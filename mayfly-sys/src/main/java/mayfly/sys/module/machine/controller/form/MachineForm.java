package mayfly.sys.module.machine.controller.form;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;


/**
 * @author meilin.huang
 * @version 1.0
 * @date 2019-11-05 4:14 下午
 */
@Data
public class MachineForm {

    private Long id;

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
