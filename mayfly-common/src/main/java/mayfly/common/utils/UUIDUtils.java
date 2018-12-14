package mayfly.common.utils;

import java.util.UUID;

/**
 * @Description: uuid工具类
 * @author: hml
 * @date: 2018/6/27 下午2:40
 */
public class UUIDUtils {

    public static String generateUUID() {
        return UUID.randomUUID().toString();
    }
}
