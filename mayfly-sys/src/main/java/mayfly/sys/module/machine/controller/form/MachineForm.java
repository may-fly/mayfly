package mayfly.sys.module.machine.controller.form;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;


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
