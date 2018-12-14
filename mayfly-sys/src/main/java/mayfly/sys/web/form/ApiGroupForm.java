package mayfly.sys.web.form;

import lombok.Data;
import mayfly.common.validation.annotation.NotBlank;
import mayfly.common.validation.annotation.Size;

/**
 * @author meilin.huang
 * @version 1.0
 * @date 2018-12-13 6:00 PM
 */
@Data
public class ApiGroupForm {
    @NotBlank
    @Size(min = 2, max = 10)
    private String name;

    private String description;
}
