package mayfly.common.result;

import java.io.Serializable;
import java.util.List;

/**
 * @author meilin.huang
 * @version 1.0
 * @date 2018-11-23 5:35 PM
 */
public class PageResult<T> implements Serializable {
    private Long total;

    private List<T> dataList;

    public PageResult(Long total, List<T> dataList) {
        this.total = total;
        this.dataList = dataList;
    }

    public List<T> getDataList() {
        return dataList;
    }

    public void setDataList(List<T> data) {
        this.dataList = data;
    }

    public Long getTotal() {
        return total;
    }

    public void setTotal(Long total) {
        this.total = total;
    }
}
