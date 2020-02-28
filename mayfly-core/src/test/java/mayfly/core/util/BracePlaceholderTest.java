package mayfly.core.util;

import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

public class BracePlaceholderTest {

    private static final String placeholder = "user:{userId}:name:{name}:{name}:{name}";

    @Test
    public void resolve() {
        System.out.println(BracePlaceholder.resolve(placeholder, 1, "哈哈哈"));
    }

    @Test
    public void resolveAll() {
        System.out.println(BracePlaceholder.resolve(placeholder, 1, "哈哈哈"));
    }

    @Test
    public void resolveByMap() {
        Map<String, Object> param = new HashMap<>();
        param.put("userId", 1);
        param.put("name", "{哈哈先生}");
        System.out.println(BracePlaceholder.resolveByMap(placeholder, param));
    }
}