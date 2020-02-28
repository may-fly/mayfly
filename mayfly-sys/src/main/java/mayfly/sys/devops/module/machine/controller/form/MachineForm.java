package mayfly.sys.devops.module.machine.controller.form;

import lombok.Data;
import mayfly.core.validation.annotation.NotBlank;
import mayfly.core.validation.annotation.NotNull;

/**
 * @author meilin.huang
 * @version 1.0
 * @date 2019-11-05 4:14 下午
 */
@Data
public class MachineForm {

    private Integer id;

    @NotBlank
    private String name;

    @NotBlank
    private String ip;

    @NotNull
    private Integer port;

    @NotBlank
    private String username;

    private String password;
}
