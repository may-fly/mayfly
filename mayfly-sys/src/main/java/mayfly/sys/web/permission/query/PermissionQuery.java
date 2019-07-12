package mayfly.sys.web.permission.query;

import lombok.Data;

/**
 * @author meilin.huang
 * @version 1.0
 * @date 2019-07-06 15:17
 */
@Data
public class PermissionQuery {

    private Integer groupId;

    /**
     * 字符串非空检验
     */
    private String uriPattern;

    private String code;

    private Integer status;
}
