package mayfly.sys.module.sys.controller.form;

import lombok.Data;
import mayfly.core.log.annotation.LogChange;

import jakarta.validation.constraints.NotBlank;

/**
 * @author meilin.huang
 * @version 1.0
 * @date 2018-12-20 9:35 AM
 */
@Data
public class RoleForm {

    private Long id;

    @LogChange(name = "角色名")
    @NotBlank
    private String name;

    @NotBlank
    private String remark;

    private String resourceIds;
}
