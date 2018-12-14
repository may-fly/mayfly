package mayfly.sys.web.form;

import lombok.Data;
import mayfly.common.validation.annotation.NotBlank;
import mayfly.common.validation.annotation.Size;

/**
 * @author meilin.huang
 * @version 1.0
 * @date 2018-12-11 11:18 AM
 */
@Data
public class MenuForm {

    private Integer id;

    private Integer pid;

    @NotBlank
    @Size(min = 2, max = 6)
    private String name;

    private String path;

    private String icon;

    private Integer weight;
}
