package mayfly.common.result;

import java.io.Serializable;
import java.util.Collections;
import java.util.List;

/**
 * @author meilin.huang
 * @version 1.0
 * @date 2018-11-23 5:35 PM
 */
public class Page<T> implements Serializable {
    private Long total;

    private List<T> list;

    private Page(Long total, List<T> list) {
        this.total = total;
        this.list = list;
    }

    public static <T> Page<T> with(Long total, List<T> data) {
        return new Page<>(total, data);
    }

    public static <T> Page<T> empty() {
        return new Page<T>(0L, Collections.emptyList());
    }

    public List<T> getList() {
        return list;
    }

    public void setList(List<T> data) {
        this.list = data;
    }

    public Long getTotal() {
        return total;
    }

    public void setTotal(Long total) {
        this.total = total;
    }
}
