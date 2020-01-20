package mayfly.sys.devops.module.redis.controller.vo;

import lombok.Builder;
import lombok.Data;
import mayfly.sys.devops.redis.commands.KeyInfo;

import java.util.List;

/**
 * @author meilin.huang
 * @version 1.0
 * @date 2019-01-22 10:57 AM
 */
@Data
@Builder
public class KeyScanVO {

    private String cursor;

    private List<KeyInfo> keys;

    private long dbsize;
}
