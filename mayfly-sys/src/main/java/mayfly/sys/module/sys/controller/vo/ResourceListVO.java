package mayfly.sys.module.sys.controller.vo;

import lombok.Getter;
import lombok.Setter;
import mayfly.core.util.TreeUtils;

import java.util.List;
import java.util.Objects;

/**
 * @author meilin.huang
 * @version 1.0
 * @date 2019-07-27 21:55
 */
@Getter
@Setter
public class ResourceListVO implements TreeUtils.TreeNode<Integer> {

    private Integer id;

    private Integer pid;

    private Integer type;

    private String name;

    private String icon;

    private String code;

    private Integer status;

    private List<ResourceListVO> children;

    @Override
    public Integer id() {
        return this.getId();
    }

    @Override
    public Integer parentId() {
        return this.pid;
    }

    @Override
    public boolean root() {
        return Objects.equals(this.pid, 0);
    }

    @Override
    public void setChildren(List children) {
        this.children = children;
    }
}
