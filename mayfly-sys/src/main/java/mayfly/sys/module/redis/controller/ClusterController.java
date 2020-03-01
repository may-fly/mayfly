package mayfly.sys.module.redis.controller;

import mayfly.core.result.Result;
import mayfly.sys.module.redis.commands.cluster.ClusterCommand;
import mayfly.sys.module.redis.service.RedisService;
import mayfly.sys.module.redis.controller.vo.RedisClusterNodeVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author meilin.huang
 * @version 1.0
 * @date 2019-01-10 10:30 AM
 */
@RequestMapping("/open/redis/cluster")
@RestController
public class ClusterController {

    @Autowired
    private RedisService redisService;

    @GetMapping("/{id}/info")
    public Result info(@PathVariable Integer id) {
        return Result.success().with(ClusterCommand.clusterInfo(redisService.getClusterCmds(id)));
    }

    @GetMapping("/{id}/nodes")
    public Result nodes(@PathVariable Integer id) {
        List<RedisClusterNodeVO> result = ClusterCommand.getNodes(id).stream().map(x -> {
            RedisClusterNodeVO vo = RedisClusterNodeVO.builder()
                    .address(x.getUri().getHost() + ":" + x.getUri().getPort())
                    .nodeId(x.getNodeId()).slaveof(x.getSlaveOf()).configEpoch(x.getConfigEpoch())
                    .flags(x.getFlags()).build();
            List<Integer> slots = x.getSlots();
            if (!slots.isEmpty()) {
                vo.setStartSlots(slots.get(0));
                vo.setEndSlogts(slots.get(slots.size() - 1));
            }

            return vo;
        }).collect(Collectors.toList());

        return Result.success().with(result);
    }

//    @MethodLog("获取集群redis key")
//    @GetMapping("/{id}/scan")
//    public Result scan(@PathVariable Integer id, Integer count, String match) {
//        RedisClusterCommands<String, byte[]> clusterCmds = redisService.getClusterCmds(id);
//        KeyScanVO scan = KeyValueCommand.clusterScan(clusterCmds, count,  match);
//        scan.setDbsize(ServerCommand.dbsize(clusterCmds));
//        return Result.success().with(scan);
//    }
//
//    @MethodLog(value = "查询redis value")
//    @GetMapping("/{id}/value")
//    public Result value(@PathVariable Integer id, String key) {
//        return Result.success().with(KeyValueCommand.value(redisService.getClusterCmds(id), key));
//    }
//
//    @MethodLog(value = "新增集群key value")
//    @PostMapping("/{id}/value")
//    public Result addKeyValue(@PathVariable Integer id, @Valid KeyValueForm keyValue) {
//        KeyValueCommand.addKeyValue(redisService.getClusterCmds(id), BeanUtils.copyProperties(keyValue, KeyInfo.class));
//        return Result.success();
//    }
}
