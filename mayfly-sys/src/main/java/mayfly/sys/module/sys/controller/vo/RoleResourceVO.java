package mayfly.sys.module.sys.controller.vo;

import lombok.Getter;
import lombok.Setter;
import mayfly.core.util.TreeUtils;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

/**
 * @author meilin.huang
 * @date 2020-03-28 2:34 下午
 */
@Getter
@Setter
public class RoleResourceVO implements TreeUtils.TreeNode<Integer> {

    private Integer resourceId;

    private Integer resourcePid;

    private String resourceName;

    private Integer status;

    private Integer type;

    private LocalDateTime createTime;

    private String createAccount;

    private List<RoleResourceVO> children;

    @Override
    public Integer id() {
        return this.resourceId;
    }

    @Override
    public Integer parentId() {
        return this.resourcePid;
    }

    @Override
    public boolean root() {
        return Objects.equals(this.resourcePid, 0);
    }

    @Override
    public void setChildren(List children) {
        this.children = children;
    }
}
