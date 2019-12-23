package mayfly.sys.module.sys.controller.form;

import lombok.Data;
import mayfly.core.validation.annotation.NotBlank;

/**
 * @author meilin.huang
 * @version 1.0
 * @date 2018-11-22 10:28 AM
 */
@Data
public class AccountLoginForm {

    @NotBlank
    private String username;

    @NotBlank
    private String password;

    //    @NotBlank
    private String captcha;
}
