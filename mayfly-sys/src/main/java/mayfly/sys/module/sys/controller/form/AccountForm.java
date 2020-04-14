package mayfly.sys.module.sys.controller.form;

import lombok.Data;

import javax.validation.constraints.NotBlank;

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
