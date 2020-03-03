package mayfly.core.util;


import org.junit.Test;

public class HttpUtilsTest {

    private static String url = "http://localhost:8080/";

    @Test
    public void testGet() {
        System.out.println(HttpUtils.get(url + "sys/resources",
                MapUtils.hashMapBuilder(2).put("status", 1).build()));

        System.out.println(HttpUtils.get(url + "sys/accounts",
                MapUtils.hashMapBuilder(2).put("pageNum", 1).put("pageSize", 1).build()));
    }

    @Test
    public void testPost() {
        System.out.println( HttpUtils.postJson(url + "sys/resources",
                MapUtils.hashMapBuilder(2).put("name", "teest").put("pageSize", 1).build()));;
    }
}