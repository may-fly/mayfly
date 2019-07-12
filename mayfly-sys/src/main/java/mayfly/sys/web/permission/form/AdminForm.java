package mayfly.sys.web.permission.form;

import lombok.Data;
import mayfly.common.validation.annotation.NotBlank;

/**
 * @author meilin.huang
 * @version 1.0
 * @date 2019-07-06 15:11
 */
@Data
public class AdminForm {
    @NotBlank
    private String username;

    @NotBlank
    private String password;
}
