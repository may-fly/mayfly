package mayfly.sys.module.open.controller.form;

import lombok.Getter;
import lombok.Setter;

import jakarta.validation.constraints.NotBlank;

/**
 * @author meilin.huang
 * @version 1.0
 * @date 2018-11-22 10:28 AM
 */
@Getter
@Setter
public class AccountLoginForm {

    @NotBlank(message = "用户名不能为空")
    private String username;

    @NotBlank(message = "密码不能为空")
    private String password;

    @NotBlank(message = "验证码不能为空")
    private String captcha;

    @NotBlank(message = "uuid不能为空")
    private String uuid;

    @Override
    public String toString() {
        return "AccountLoginForm{" +
                "username='" + username + '\'' +
                ", password=' ******" + '\'' +
                ", captcha='******" + '\'' +
                ", uuid='" + uuid + '\'' +
                '}';
    }
}
