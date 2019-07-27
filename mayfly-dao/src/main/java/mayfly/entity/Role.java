package mayfly.entity;

import lombok.Data;
import mayfly.dao.base.annotation.NoColumn;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

/**
 * @Description: 角色
 * @author: hml
 * @date: 2018/6/27 下午2:03
 */
@Data
public class Role implements Serializable {

    private Integer id;

    private String name;

    private String remark;

    private Byte status;

    private LocalDateTime createTime;

    private LocalDateTime updateTime;

    @NoColumn
    private List<Resource> resources;
}
