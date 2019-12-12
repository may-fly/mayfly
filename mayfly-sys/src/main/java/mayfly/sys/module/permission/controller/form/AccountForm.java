package mayfly.sys.module.permission.controller.form;

import lombok.Data;
import mayfly.core.validation.annotation.NotBlank;

/**
 * @author meilin.huang
 * @version 1.0
 * @date 2019-07-06 15:11
 */
@Data
public class AccountForm {
    @NotBlank
    private String username;

    @NotBlank
    private String password;
}
