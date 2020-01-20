package mayfly.core.util;

import org.junit.Test;

import java.util.Map;

public class MapUtilsTest {

    @Test
    public void newHashMap() {

        Map<String, Integer> map1 = MapUtils.<String, Integer>hashMapBuilder(1)
                .put("1", 2)
                .put("2", 1)
                .build();

        Map<Integer, Integer> map2 = MapUtils.hashMapBuilder(8, 1, 1)
                .put(2, 1)
                .build();
    }
}