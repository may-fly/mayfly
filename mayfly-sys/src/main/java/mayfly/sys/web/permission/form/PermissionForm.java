package mayfly.sys.web.permission.form;

import lombok.Data;
import mayfly.common.enums.StatusEnum;
import mayfly.common.validation.annotation.EnumValue;
import mayfly.common.validation.annotation.NotBlank;
import mayfly.common.validation.annotation.NotNull;
import mayfly.sys.common.enums.MethodEnum;

/**
 * @author meilin.huang
 * @version 1.0
 * @date 2018-11-26 9:14 AM
 */
@Data
public class PermissionForm {

    private Integer groupId;

    /**
     * 字符串非空检验
     */
    @NotBlank
    private String uriPattern;

    @NotBlank
    private String code;

    /**
     * method不能为空且只能是MethodEnum中对应的枚举值value
     */
    @EnumValue(MethodEnum.class)
    @NotNull
    private Integer method;

    @NotBlank
    private String description;

    @EnumValue(StatusEnum.class)
    private Integer status;
}
