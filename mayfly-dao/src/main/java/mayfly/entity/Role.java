package mayfly.entity;

import lombok.Data;
import mayfly.dao.base.annotation.NoColumn;

import java.io.Serializable;
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

    private Integer menuId;

    private Byte status;

    @NoColumn
    private List<Menu> menus;
}
