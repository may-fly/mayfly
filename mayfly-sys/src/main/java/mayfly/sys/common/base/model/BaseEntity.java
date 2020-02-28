package mayfly.sys.common.base.model;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

/**
 * @author meilin.huang
 * @date 2020-02-21 7:07 下午
 */
@Getter
@Setter
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
}
