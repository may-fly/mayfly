package mayfly.common.utils;

import mayfly.common.log.MethodLog;

/**
 * @Description: String工具类
 * @author: hml
 * @date: 2018/6/14 下午3:01
 */
public class StringUtils {
    @MethodLog
    public static boolean isEmpty(String str) {
        return str == null || str.trim().length() == 0;
    }

//    public static void main(String[] args) {
//        isEmpty("");
//    }
}
