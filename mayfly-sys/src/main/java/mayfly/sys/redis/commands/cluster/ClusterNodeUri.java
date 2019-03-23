package mayfly.sys.redis.commands.cluster;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

/**
 * 集群节点基本uri信息
 * @author meilin.huang
 * @version 1.0
 * @date 2019-01-09 11:02 AM
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ClusterNodeUri {
    private String host;

    private int port;

    private String password;

    private int slotsBegin;
    private int slotsEnd;

    /**
     * 从节点列表
     */
    private List<ClusterNodeUri> slaves;

    public ClusterNodeUri(String host, int port, String password) {
        this.host = host;
        this.port = port;
        this.password = password;
    }

    /**
     * 添加从节点信息
     * @param slaveUri
     * @return
     */
    public ClusterNodeUri addSlave(ClusterNodeUri slaveUri) {
        if (this.slaves == null) {
            this.slaves = new ArrayList<>(4);
        }
        this.slaves.add(slaveUri);
        return this;
    }
}
