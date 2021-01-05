package mayfly.sys.common.utils;


import mayfly.core.util.ArrayUtils;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * @author meilin.huang
 * @date 2020-06-28 3:43 下午
 */
public class SignUtils {

    /**
     * 按参数名升续拼接参数
     *
     * @param request      request
     * @param ignoreParams 忽略的参数名
     * @return result
     */
    public static String concatSignString(HttpServletRequest request, String... ignoreParams) {
        return concatSignString(request, true, ignoreParams);
    }

    /**
     * 按参数名升续拼接参数
     *
     * @param request          request
     * @param ignoreEmptyValue 是否忽略空值 null or empty
     * @param ignoreParams     忽略的参数名
     * @return result
     */
    public static String concatSignString(HttpServletRequest request, boolean ignoreEmptyValue, String... ignoreParams) {
        Map<String, Object> parameterMap = new HashMap<>();
        request.getParameterMap().forEach((key, value) -> parameterMap.put(key, value[0]));
        return concatSignString(parameterMap, ignoreEmptyValue, ignoreParams);
    }

    /**
     * 按参数名升续拼接参数
     *
     * @param parameterMap     参数map
     * @param ignoreEmptyValue 是否忽略空值 null or empty
     * @param ignoreKey        忽略的key值
     * @return result
     */
    public static String concatSignString(Map<String, Object> parameterMap, boolean ignoreEmptyValue, String... ignoreKey) {
        // 按照key升续排序，然后拼接参数
        Set<String> keySet = parameterMap.keySet();
        String[] keyArray = keySet.toArray(new String[0]);
        Arrays.sort(keyArray);
        StringBuilder sb = new StringBuilder();
        boolean first = true;
        for (String k : keyArray) {
            // 忽略掉的字段
            if (ArrayUtils.contains(ignoreKey, k)) {
                continue;
            }
            Object v = parameterMap.get(k);
            boolean empty = v == null || v.toString().trim().length() == 0;
            if (empty && ignoreEmptyValue) {
                continue;
            }

            if (!first) {
                sb.append("&");
            }
            sb.append(k).append("=");
            if (!empty) {
                sb.append(v);
            }
            if (first) {
                first = false;
            }
        }
        return sb.toString();
    }
}
