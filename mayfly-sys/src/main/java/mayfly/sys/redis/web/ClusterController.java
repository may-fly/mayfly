package mayfly.sys.redis.web;

import io.lettuce.core.KeyScanCursor;
import io.lettuce.core.cluster.api.sync.RedisClusterCommands;
import mayfly.common.result.Result;
import mayfly.sys.redis.commands.KeyCommand;
import mayfly.sys.redis.commands.ServerCommand;
import mayfly.sys.redis.commands.cluster.ClusterCommand;
import mayfly.sys.redis.service.RedisService;
import mayfly.sys.redis.web.vo.KeyScanVO;
import mayfly.sys.redis.web.vo.RedisClusterNodeVO;
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
    public Result scan(@PathVariable Integer id, String match) {
        RedisClusterCommands<String, byte[]> clusterCmds = redisService.getClusterCmds(id);
        KeyScanCursor<String> scan = KeyCommand.scan(clusterCmds, 500,  match);
        KeyScanVO vo = KeyScanVO.builder().cursor(scan.getCursor()).keys(scan.getKeys()).dbsize(ServerCommand.dbsize(clusterCmds)).build();
        return Result.success().withData(vo);
    }

//    @GetMapping("/{id}/keys")
//    public Result keys(@PathVariable Integer id) {
//        return Result.success().withData(KeyCommand.keys(redisService.getClusterCmds(id)));
//    }
}
