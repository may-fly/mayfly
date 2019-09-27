package mayfly.sys.web.permission.form;

import lombok.Data;
import mayfly.common.validation.annotation.EnumValue;
import mayfly.common.validation.annotation.NotBlank;
import mayfly.common.validation.annotation.NotNull;
import mayfly.common.validation.annotation.Size;
import mayfly.sys.common.enums.ResourceTypeEnum;

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

    private String path;

    private String code;

    @NotNull
    @EnumValue(ResourceTypeEnum.class)
    private Integer type;

    private String icon;

    @NotNull
    private Integer weight;
}
