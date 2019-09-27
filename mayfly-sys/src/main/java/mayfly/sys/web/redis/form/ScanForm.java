package mayfly.sys.web.redis.form;

import lombok.Data;
import mayfly.core.validation.annotation.Size;

/**
 * @author meilin.huang
 * @version 1.0
 * @date 2019-04-03 09:51
 */
@Data
public class ScanForm {
    private String cursor;

    @Size(min = 1, max = 20)
    private Integer count;

    private String match;
}
