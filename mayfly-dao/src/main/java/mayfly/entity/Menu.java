package mayfly.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import mayfly.dao.base.annotation.NoColumn;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

/**
 * @Description: 菜单
 * @author: hml
 * @date: 2018/6/27 下午2:01
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Menu implements Serializable {

    private Integer id;

    private Integer pid;

    private Integer weight;

    private String name;

    private String icon;

    private String path;

    private String code;

    private Byte status ;

    private LocalDateTime createTime;

    private LocalDateTime updateTime;

    @NoColumn
    private List<Menu> children;

}
