package mayfly.sys.redis.commands.cluster;

import io.lettuce.core.*;
import io.lettuce.core.api.StatefulRedisConnection;
import io.lettuce.core.api.sync.RedisCommands;
import io.lettuce.core.cluster.api.StatefulRedisClusterConnection;
import io.lettuce.core.cluster.api.sync.RedisClusterCommands;
import io.lettuce.core.cluster.models.partitions.Partitions;
import io.lettuce.core.cluster.models.partitions.RedisClusterNode;
import mayfly.common.exception.BusinessRuntimeException;
import mayfly.common.utils.StringUtils;
import mayfly.sys.redis.connection.RedisConnectionRegister;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

/**
 * @author meilin.huang
 * @version 1.0
 * @date 2019-01-19 10:08 AM
 */
public class ClusterCommand {

    private static RedisConnectionRegister register = RedisConnectionRegister.getInstance();

    private static final Logger LOG = LoggerFactory.getLogger(ClusterCommand.class);

//    private static ClusterCommand instance = new ClusterCommand();
//
//    public static ClusterCommand getInstance() {
//        return instance;
//    }
//
//    private ClusterCommand(){}


    public static StatefulRedisClusterConnection getConnection(int clusterId) {
        return register.getClusterConnection(clusterId);
    }

    public static RedisClusterCommands<String, byte[]> getCmds(int clusterId) {
        return register.getClusterCmds(clusterId);
    }

    public static RedisCommands<String, byte[]> getNodeCmds(int cluster, String nodeId) {
        return getConnection(cluster).getConnection(nodeId).sync();
    }

    public static RedisCommands<String, byte[]> getNodeCmds(int cluster, String host, int port) {
        return getConnection(cluster).getConnection(host, port).sync();
    }


    public static Map<String, Object> clusterInfo(int clusterId) {
        String info = getCmds(clusterId).clusterInfo();
        Map<String, Object> result = new HashMap<>();
        String[] infos = info.split("\r\n");
        for (String i : infos) {
            String[] nameAndValue = i.split(":");
            result.put(nameAndValue[0], nameAndValue[1]);
        }
        return result;
    }

    /**
     * 获取集群分区信息
     *
     * @return
     */
    public static Partitions getPartitions(int clusterId) {
        return getConnection(clusterId).getPartitions();
    }

    /**
     * 获取集群所有节点
     *
     * @return
     */
    public static List<RedisClusterNode> getNodes(int clusterId) {
        return getPartitions(clusterId).getPartitions();
    }

    /**
     * 根据nodeId获取该分区指定节点信息
     *
     * @param nodeId
     * @return
     */
    public static RedisClusterNode getNode(int clusterId, String nodeId) {
        return getPartitions(clusterId).getPartitionByNodeId(nodeId);
    }

    /**
     * 根据host和port获取该分区指定节点信息
     * @param host
     * @return
     */
    public static RedisClusterNode getNode(int clusterId, String host, int port) {
        return getPartitions(clusterId).getPartition(host, port);
    }



    /**
     * 创建redis集群
     * @param clusterUris
     * @throws InterruptedException
     */
    public static void createCluster(Collection<ClusterNodeUri> clusterUris) throws InterruptedException {
        // 所有cluster节点
        List<ClusterNode> clusterNodeList = new ArrayList<>();
        // master节点
        List<ClusterNode> masterNodeList = new ArrayList<>(clusterUris.size());

        //创建并连接所有master节点以及master对应slave节点
        clusterUris.forEach(m -> {
            final ClusterNode master = ClusterNode.connect(m);
            clusterNodeList.add(master);
            masterNodeList.add(master);

            //添加并连接slave节点
            Optional.ofNullable(m.getSlaves()).ifPresent(slaves -> {
                slaves.forEach(s -> {
                    ClusterNode slave = ClusterNode.connect(s);
                    master.addSlave(slave);
                    clusterNodeList.add(slave);
                });
            });
        });

        // 执行cluster meet命令
        doClusterMeet(clusterNodeList);

        // 为主节点指派slot,将16384个slot分成三份：5461，5461，5462
        int[] slots = {0, 5460, 5461, 10921, 10922, 16383};
        int index = 0;
        for (ClusterNode node : masterNodeList) {
            node.getNodeUri().setSlotsBegin(slots[index]);
            index++;
            node.getNodeUri().setSlotsEnd(slots[index]);
            index++;
        }
        // 通过与各个主节点的连接，执行addSlots命令为主节点指派slot
        for (ClusterNode node : masterNodeList) {
            try {
                node.getCmds().clusterAddSlots(createSlots(node.getNodeUri().getSlotsBegin(), node.getNodeUri().getSlotsEnd()));
            } catch (RedisCommandTimeoutException | RedisConnectionException e) {
                LOG.error("add slots failed --> {}:{}", node.getNodeUri().getHost(), node.getNodeUri().getPort());
            }
        }
        // 延时1s，等待slot指派完成
        Thread.sleep(1000);
        // 为已经指派slot的主节点设置从节点,6379,6380,6381分别对应6382，6383，6384
        for (ClusterNode master : masterNodeList) {
            Optional.ofNullable(master.getSlaves()).ifPresent(slaves -> {
                slaves.forEach(s -> {
                    s.getCmds().clusterReplicate(master.getMyId());
                });
            });
        }
        // 关闭连接,销毁客户端，释放资源
        for (ClusterNode node : clusterNodeList) {
            node.close();
        }
    }

    /**
     * 执行cluster meet命令使各个孤立的节点相互感知，初步形成集群。
     * 只需以一个节点为基准，让所有节点与之meet即可
     * @param clusterNodes
     */
    private static void doClusterMeet(List<ClusterNode> clusterNodes) {
        ClusterNode first = clusterNodes.get(0);
        String firstHost = first.getNodeUri().getHost();
        int firstPort = first.getNodeUri().getPort();

        for (int i = 1, size = clusterNodes.size(); i < size; i++) {
            clusterNodes.get(i).getCmds().clusterMeet(firstHost, firstPort);
        }
    }

    private static int[] createSlots(int from, int to) {
        int[] result = new int[to - from + 1];
        int counter = 0;
        for (int i = from; i <= to; i++) {
            result[counter++] = i;
        }
        return result;
    }

    /**
     * 添加节点
     */
    public static void addNode(int clusterId, ClusterNodeUri uri) {
        ClusterNode node = ClusterNode.connect(uri);
        getCmds(clusterId).clusterMeet(uri.getHost(), uri.getPort());
        node.close();
    }

    /**
     * 向指定节点添加从节点
     *
     * @param nodeId  主节点nodeId
     * @param nodeUri 从节点uri
     */
    public static void addReplicate(int clusterId, String nodeId, ClusterNodeUri nodeUri) {
        //添加节点到集群中
        addNode(clusterId, nodeUri);
        getNodeCmds(clusterId, nodeId).clusterReplicate(getNode(clusterId, nodeUri.getHost(), nodeUri.getPort()).getNodeId());
    }

    /**
     * 向指定节点添加从节点
     *
     * @param masterUri 主节点uri
     * @param nodeUri   从节点uri
     */
    public static void addReplicate(int clusterId, ClusterNodeUri masterUri, ClusterNodeUri nodeUri) {
        //添加节点到集群中
        addNode(clusterId, nodeUri);
        getNodeCmds(clusterId, masterUri.getHost(), masterUri.getPort())
                .clusterReplicate(getNode(clusterId, nodeUri.getHost(), nodeUri.getPort()).getNodeId());
    }


    /**
     * 删除节点
     * @param rmNodeId
     */
    public static void delNode(int clusterId, String rmNodeId) {
        RedisClusterNode rmNode = getNode(clusterId, rmNodeId);
        if (rmNode.is(RedisClusterNode.NodeFlag.MASTER) && !rmNode.getSlots().isEmpty()) {
            LOG.error("请先将该master分支上的slots移除！ url: {}:{}", rmNode.getUri().getHost(), rmNode.getUri().getPort());
            throw new BusinessRuntimeException("请先将该master分支上的slots移除！");
        }

        getNodes(clusterId).stream().filter(node -> {
            //判断是否是myself
            if (node.getNodeId().equals(rmNodeId)) {
                return false;
            }
            //从节点不能删除自身的主节点
            if (node.is(RedisClusterNode.NodeFlag.SLAVE) && node.getSlaveOf().equals(rmNodeId)) {
                return false;
            }

            return true;
        }).forEach(node -> forget(clusterId, node, rmNode));
    }

    /**
     * 执行clusterForget命令
     *
     * @param myselfNode
     * @param rmNode
     */
    private static void forget(int clusterId, RedisClusterNode myselfNode, RedisClusterNode rmNode) {
        if (rmNode.getNodeId().equals(myselfNode.getNodeId())) {
            LOG.error("不能删除自己！ url: {}:{}", myselfNode.getUri().getHost(), myselfNode.getUri().getPort());
            throw new BusinessRuntimeException("不能删除自己！");
        }

        if (myselfNode.is(RedisClusterNode.NodeFlag.SLAVE)) {
            if (myselfNode.getSlaveOf().equals(rmNode.getNodeId())) {
                LOG.error("不能删除自身的master节点！ url: {}:{}", myselfNode.getUri().getHost(), myselfNode.getUri().getPort());
                throw new BusinessRuntimeException("不能删除自身的master节点！");
            }
        }

        getNodeCmds(clusterId, myselfNode.getNodeId()).clusterForget(rmNode.getNodeId());
    }


    /**
     * 用于创建并连接要新建的集群节点
     */
    private static class ClusterNode{
        private ClusterNodeUri nodeUri;
        private String myId;
        private StatefulRedisConnection<String, String> connection;
        private RedisClient redisClient;
        /**
         * slave节点
         */
        private List<ClusterNode> slaves;

        public ClusterNode(ClusterNodeUri nodeUri) {
            this.nodeUri = nodeUri;
        }

        /**
         * 连接node
         * @param uri
         * @return
         */
        public static ClusterNode connect(ClusterNodeUri uri) {
            ClusterNode master =  new ClusterNode(uri);
            master.connect();
            return master;
        }

        /**
         * 连接redis节点，并获取nodeId
         */
        private void connect() {
            String host = nodeUri.getHost();
            int port = nodeUri.getPort();
            String password = nodeUri.getPassword();

            RedisURI redisUri = RedisURI.Builder.redis(host, port).build();
            if (!StringUtils.isEmpty(password)) {
                redisUri.setPassword(password);
            }
            try {
                redisClient = RedisClient.create(redisUri);
                connection = redisClient.connect();
                myId = getCmds().clusterMyId();
            } catch (RedisConnectionException e) {
                LOG.error("redis连接失败， {}:{}", host, port, e);
                throw new BusinessRuntimeException("redis连接失败！ url: " + host + ":" + String.valueOf(port));
            } catch (RedisCommandExecutionException e) {
                LOG.error("该节点不支持集群， {}:{}", host, port, e);
                throw new BusinessRuntimeException("该节点不支持集群，请在redis.conf中启用'cluster-enabled yes！' 节点: " + host + ":" + String.valueOf(port));
            }
        }

        /**
         * 添加slave节点
         * @param slave
         */
        public ClusterNode addSlave(ClusterNode slave) {
            if (slaves == null) {
                slaves = new ArrayList<>(4);
            }
            slaves.add(slave);
            return this;
        }

        public RedisCommands getCmds() {
            return this.connection.sync();
        }

        public void close() {
            redisClient.shutdown();
            redisClient = null;
            connection.close();
            connection = null;
        }

        public ClusterNodeUri getNodeUri() {
            return nodeUri;
        }

        public String getMyId() {
            return myId;
        }

        public StatefulRedisConnection<String, String> getConnection() {
            return connection;
        }

        public RedisClient getRedisClient() {
            return redisClient;
        }

        public List<ClusterNode> getSlaves() {
            return slaves;
        }
    }
}
