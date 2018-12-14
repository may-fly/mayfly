package mayfly.entity;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @Description: 权限
 * @author: hml
 * @date: 2018/6/27 下午2:35
 */
public class Permission implements Serializable {

    private Integer id;

    private Integer userId;

    private Integer roleId;

    private LocalDateTime createTime;
}
