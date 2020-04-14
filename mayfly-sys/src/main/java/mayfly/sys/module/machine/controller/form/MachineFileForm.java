package mayfly.sys.module.machine.controller.form;

import lombok.Data;
import mayfly.core.validation.annotation.EnumValue;
import mayfly.sys.module.machine.enums.MachineFileTypeEnum;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * @author meilin.huang
 * @version 1.0
 * @date 2019-11-05 3:42 下午
 */
@Data
public class MachineFileForm {

    @NotBlank
    private String name;

    @NotBlank
    private String path;

    @NotNull
    @EnumValue(MachineFileTypeEnum.class)
    private Integer type;
}
