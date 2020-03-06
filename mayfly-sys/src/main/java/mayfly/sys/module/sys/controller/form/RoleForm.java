package mayfly.sys.module.sys.controller.form;

import lombok.Data;
import mayfly.core.validation.annotation.NotBlank;

/**
 * @author meilin.huang
 * @version 1.0
 * @date 2018-12-20 9:35 AM
 */
@Data
public class RoleForm {

    private Integer id;

    @NotBlank
    private String name;

    @NotBlank
    private String remark;

    private String resourceIds;
}
