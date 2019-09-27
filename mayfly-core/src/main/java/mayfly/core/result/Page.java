package mayfly.core.result;

import java.io.Serializable;
import java.util.Collections;
import java.util.List;

/**
 * 分页数据
 * @author meilin.huang
 * @version 1.0
 * @date 2018-11-23 5:35 PM
 */
public class Page<T> implements Serializable {

    private static final long serialVersionUID = 5390817850198908640L;

    /**
     * 数据总数
     */
    private Long total;

    /**
     * 指定页数数据列表
     */
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

    public Long getTotal() {
        return total;
    }
}
