package mayfly.sys.devops.module.machine.controller.form;

import lombok.Data;
import mayfly.core.validation.annotation.EnumValue;
import mayfly.core.validation.annotation.NotBlank;
import mayfly.core.validation.annotation.NotNull;
import mayfly.sys.devops.module.machine.enums.MachineFileTypeEnum;

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
