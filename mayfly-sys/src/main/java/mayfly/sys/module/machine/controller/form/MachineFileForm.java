package mayfly.sys.module.machine.controller.form;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import mayfly.core.validation.annotation.EnumValue;
import mayfly.sys.module.machine.enums.MachineFileTypeEnum;

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
