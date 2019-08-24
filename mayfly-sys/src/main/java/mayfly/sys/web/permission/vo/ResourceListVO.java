package mayfly.sys.web.permission.vo;

import lombok.Data;
import mayfly.common.util.TreeUtils;
import mayfly.dao.base.annotation.NoColumn;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

/**
 * @author meilin.huang
 * @version 1.0
 * @date 2019-07-27 21:55
 */
@Data
public class ResourceListVO implements TreeUtils.TreeNode<Integer> {

    private Integer id;

    private Integer pid;

    private Integer type;

    private String name;

    private String icon;

    private String path;

    private String code;

    private Integer status ;

    private Integer weight;

    private LocalDateTime createTime;

    private LocalDateTime updateTime;

    @NoColumn
    private List<ResourceListVO> children;

    @Override
    public Integer id() {
        return this.id;
    }

    @Override
    public Integer parentId() {
        return this.pid;
    }

    @Override
    public boolean isRoot() {
        return Objects.equals(this.pid, 0);
    }

    @Override
    public void setChildren(List children) {
        this.children = children;
    }
}
