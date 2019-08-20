package mayfly.sys.web.permission.vo;

import lombok.Data;

/**
 * @author meilin.huang
 * @version 1.0
 * @date 2019-07-27 21:55
 */
@Data
public class ResourceListVO {

    private Integer id;

    private Integer pid;

    private String name;

    private String path;

    private String code;

    private Integer status;
}
