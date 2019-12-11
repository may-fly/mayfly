package mayfly.core.util;

import java.util.Map;

/**
 * @author meilin.huang
 * @version 1.0
 * @date 2019-12-04 5:29 下午
 */
public class MapUtils {

    /**
     * 判断map是否为空
     *
     * @param map map
     * @return 是否为空map
     */
    public static boolean isEmpty(Map<?, ?> map) {
        return map == null || map.isEmpty();
    }
}
