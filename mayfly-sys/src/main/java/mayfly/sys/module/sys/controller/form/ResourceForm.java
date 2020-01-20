package mayfly.sys.module.sys.controller.form;

import lombok.Data;
import mayfly.core.validation.annotation.EnumValue;
import mayfly.core.validation.annotation.NotBlank;
import mayfly.core.validation.annotation.NotNull;
import mayfly.core.validation.annotation.Size;
import mayfly.sys.module.sys.enums.ResourceTypeEnum;

/**
 * @author meilin.huang
 * @version 1.0
 * @date 2018-12-11 11:18 AM
 */
@Data
public class ResourceForm {
    private Integer pid;

    @NotBlank
    @Size(min = 2, max = 24)
    private String name;

    private String code;

    @NotNull
    @EnumValue(ResourceTypeEnum.class)
    private Integer type;

    private String icon;

    @NotNull
    private Integer weight;
}
