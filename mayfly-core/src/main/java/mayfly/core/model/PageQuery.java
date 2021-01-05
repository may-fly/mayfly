package mayfly.core.model;

/**
 * @author meilin.huang
 * @version 1.0
 * @date 2018-11-23 5:55 PM
 */
public class PageQuery {

    /**
     * 每页允许显示的最大记录数
     */
    public static final int MAX_PAGE_SIZE = 50;

    /**
     * 默认页数为1
     */
    private Integer pageNum = 1;

    /**
     * 默认每页显示10条
     */
    private Integer pageSize = 10;


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

    @Override
    public String toString() {
        return "PageQuery{" +
                "pageNum=" + pageNum +
                ", pageSize=" + pageSize +
                '}';
    }
}
