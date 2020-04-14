package mayfly.sys.module.open.controller.form;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

/**
 * @author meilin.huang
 * @version 1.0
 * @date 2018-11-22 10:28 AM
 */
@Getter
@Setter
public class AccountLoginForm {

    @NotBlank
    private String username;

    @NotBlank
    private String password;

    //    @NotBlank
    private String captcha;
}
