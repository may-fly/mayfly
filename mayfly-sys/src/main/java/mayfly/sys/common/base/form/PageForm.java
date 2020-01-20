package mayfly.sys.common.base.form;

import lombok.Data;
import mayfly.core.validation.annotation.NotNull;
import mayfly.core.validation.annotation.Size;

/**
 * @author meilin.huang
 * @version 1.0
 * @date 2018-11-23 5:55 PM
 */
@Data
public class PageForm {
    @NotNull
    private Integer pageNum;

    @NotNull
    @Size(min = 1, max = 20)
    private Integer pageSize;
}
