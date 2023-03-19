package mayfly.sys.module.redis.controller.form;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.Data;


/**
 * @author meilin.huang
 * @version 1.0
 * @date 2019-04-03 09:51
 */
@Data
public class ScanForm {
    private String cursor;

    @Min(1)
    @Max(20)
    private Integer count;

    private String match;
}
