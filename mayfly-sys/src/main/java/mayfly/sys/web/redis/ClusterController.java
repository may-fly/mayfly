package mayfly.sys.web.redis;

import io.lettuce.core.cluster.api.sync.RedisClusterCommands;
import mayfly.common.result.Result;
import mayfly.sys.redis.commands.KeyCommand;
import mayfly.sys.redis.commands.ServerCommand;
import mayfly.sys.redis.commands.cluster.ClusterCommand;
import mayfly.sys.service.redis.RedisService;
import mayfly.sys.web.redis.vo.KeyScanVO;
import mayfly.sys.web.redis.vo.RedisClusterNodeVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

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

    @PostMapping("/{id}/connect")
    public Result connect(@PathVariable Integer id) {
        redisService.connect(true, id);
        return Result.success();
    }

    @GetMapping("/{id}/info")
    public Result info(@PathVariable Integer id) {
        return Result.success().withData(ClusterCommand.clusterInfo(id));
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

        return Result.success().withData(result);
    }

    @GetMapping("/{id}/scan")
    public Result scan(@PathVariable Integer id, String cursor, Integer count, String match) {
        RedisClusterCommands<String, byte[]> clusterCmds = redisService.getClusterCmds(id);
        KeyScanVO scan = KeyCommand.scan(clusterCmds, cursor, count,  match);
        scan.setDbsize(ServerCommand.dbsize(clusterCmds));
        return Result.success().withData(scan);
    }

//    @GetMapping("/{id}/keys")
//    public Result keys(@PathVariable Integer id) {
//        return Result.success().withData(KeyCommand.keys(redisService.getClusterCmds(id)));
//    }
}
