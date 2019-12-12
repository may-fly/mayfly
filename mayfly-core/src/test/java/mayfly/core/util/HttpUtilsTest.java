package mayfly.core.util;

import mayfly.core.util.http.HttpRequest;
import mayfly.core.util.http.HttpUtils;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

public class HttpUtilsTest {

    private static String url = "http://localhost:8080/mayfly/";

    @Test
    public void get() throws Exception {
        Map<String, Object> queryParams = new HashMap<>();
        queryParams.put("userId", 11);
        queryParams.put("username", "admin");
        queryParams.put("password", "11111");
        String s = HttpUtils.get(url + "sys/v1/admins", queryParams);
        System.out.println();
    }


    @Test
    public void get2() {
        try {
            System.out.println(HttpRequest.create("https://www.baidu.com").get());
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    @Test
    public void post() throws Exception {
        Map<String, String> queryParams = new HashMap<>();
        queryParams.put("userId", "11");
        queryParams.put("username", "admin");
        queryParams.put("password", "123456");
        String s = HttpRequest.create(url + "open/v1/login11").body(queryParams).postJson();
        System.out.println();
    }
}