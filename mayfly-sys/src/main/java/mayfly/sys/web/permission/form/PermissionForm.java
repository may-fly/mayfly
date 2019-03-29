package mayfly.sys.web.permission.form;

import lombok.Data;
import mayfly.common.enums.StatusEnum;
import mayfly.common.validation.annotation.EnumValue;
import mayfly.common.validation.annotation.NotBlank;
import mayfly.common.validation.annotation.NotNull;

/**
 * @author meilin.huang
 * @version 1.0
 * @date 2018-11-26 9:14 AM
 */
@Data
public class PermissionForm {

    private Integer groupId;

    @NotBlank
    private String uriPattern;

    @NotBlank
    private String code;

    @EnumValue(clazz = StatusEnum.class)
    @NotNull
    private Integer method;

    @NotBlank
    private String description;

    @EnumValue(clazz = StatusEnum.class)
    private Integer status;
}
