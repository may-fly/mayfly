package mayfly.core.util;

import java.util.Map;
import java.util.function.Function;

/**
 * {}占位符解析工具
 *
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
        return resolver.resolve(content, objs);
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

    public static void reverTemplate(String temp, String content, Map<String, String> res) {
        int index = temp.indexOf("{");
        int endIndex = temp.indexOf("}") + 1;

        String next = temp.substring(endIndex).trim();
        int nextContain= next.indexOf("{");
        String nexIndexValue = next;
        if (nextContain != -1) {
            nexIndexValue = next.substring(0, nextContain);
        }
        String key = temp.substring(index + 1, endIndex - 1);

        int valueLastIndex;
        if ("".equals(nexIndexValue)) {
            valueLastIndex = content.length();
        } else {
            valueLastIndex = content.indexOf(nexIndexValue);
        }
        String value = content.substring(index, valueLastIndex).trim();
        res.put(key, value);

        if (nextContain != -1) {
            reverTemplate(next, content.substring(content.indexOf(value)  + value.length(), content.length()).trim(), res);
        }
    }
}
