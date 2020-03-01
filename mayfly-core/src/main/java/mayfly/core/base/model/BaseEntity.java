package mayfly.core.base.model;

import java.time.LocalDateTime;

/**
 * @author meilin.huang
 * @date 2020-02-21 7:07 下午
 */
public class BaseEntity {

    private Integer id;

    private LocalDateTime createTime;

    private LocalDateTime updateTime;

    /**
     * 自动设置基本信息，默认id为空时设置新建需要的基本信息
     */
    public void autoSetBaseInfo() {
        this.autoSetBaseInfo(this.id == null);
    }

    /**
     * 自动设置基本信息
     */
    public void autoSetBaseInfo(boolean isCreate) {
        LocalDateTime now = LocalDateTime.now();
        if (isCreate) {
            this.createTime = now;
        }
        updateTime = now;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public LocalDateTime getCreateTime() {
        return createTime;
    }

    public void setCreateTime(LocalDateTime createTime) {
        this.createTime = createTime;
    }

    public LocalDateTime getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(LocalDateTime updateTime) {
        this.updateTime = updateTime;
    }
}
