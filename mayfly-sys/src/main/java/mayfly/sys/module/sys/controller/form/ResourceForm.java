package mayfly.sys.module.sys.controller.form;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import mayfly.core.log.annotation.LogChange;
import mayfly.core.validation.annotation.EnumValue;
import mayfly.sys.module.sys.enums.ResourceTypeEnum;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Map;

/**
 * @author meilin.huang
 * @version 1.0
 * @date 2018-12-11 11:18 AM
 */
@Getter
@Setter
@ToString
public class ResourceForm {
    private Long pid;

    @NotBlank
    @Size(min = 2, max = 24)
    @LogChange(name = "资源名称")
    private String name;

    @LogChange(name = "资源code")
    private String code;

    private Map<String, Object> meta;

    @NotNull
    @EnumValue(value = ResourceTypeEnum.class, name = "资源类型")
    private Integer type;

    @LogChange(name = "顺序")
    @NotNull
    private Integer weight;
}
