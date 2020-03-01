package mayfly.core.base.model;

import mayfly.core.validation.annotation.NotNull;
import mayfly.core.validation.annotation.Size;

/**
 * @author meilin.huang
 * @version 1.0
 * @date 2018-11-23 5:55 PM
 */
public class PageQuery {
    @NotNull
    private Integer pageNum;

    @NotNull
    @Size(min = 1, max = 20)
    private Integer pageSize;

    public Integer getPageNum() {
        return pageNum;
    }

    public void setPageNum(Integer pageNum) {
        this.pageNum = pageNum;
    }

    public Integer getPageSize() {
        return pageSize;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }
}
