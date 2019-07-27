package mayfly.common.util;

import java.util.Map;
import java.util.function.Function;

/**
 * {}占位符解析工具
 * @author meilin.huang
 * @version 1.0
 * @date 2019-06-16 20:32
 */
public class BracePlaceholder {

    /**
     * 花括号占位符解析器
     */
    private static PlaceholderResolver resolver = PlaceholderResolver.getResolver("{", "}");

    public static String resolve(String content, Object... objs) {
        return resolver.resolveByObject(content, objs);
    }

    public static String resolveByMap(String content, Map<String, Object> map) {
        return resolver.resolveByMap(content, map);
    }

    public static String resolveByObject(String content, Object object) {
        return resolver.resolveByObject(content, object);
    }

    public static String resolveByRule(String content, Function<String, String> rule) {
        return resolver.resolveByRule(content, rule);
    }
}
