package mayfly.core.util;

import mayfly.core.util.bean.BeanUtils;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Map;

/**
 * @author meilin.huang
 * @date 2020-03-02 9:35 上午
 */
public class HttpUtils {

    private static HttpClient httpClient = HttpClient.newHttpClient();

    /**
     * 发送get请求
     *
     * @param url url
     * @return response
     */
    public static String get(String url) {
        var httpGet = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .GET()
                .build();
        try {
            var httpResponse = httpClient.send(httpGet, HttpResponse.BodyHandlers.ofString());
            return httpResponse.body();
        } catch (IOException | InterruptedException e) {
            throw new IllegalArgumentException(e);
        }
    }

    /**
     * 发送带有查询参数的get请求
     *
     * @param url    url
     * @param params 参数（可以是map也可以使普通对象）
     * @return response
     */
    public static String get(String url, Object params) {
        return get(url + parseQueryParam(params));
    }

    /**
     * 用Content-Type=application/json形式发送post请求
     *
     * @param url      url
     * @param jsonBody 请求体
     * @return response
     */
    public static String postJson(String url, Object jsonBody) {
        var post = HttpRequest.newBuilder()
                .header("Content-Type", "application/json")
                .uri(URI.create(url))
                .POST(HttpRequest.BodyPublishers.ofString(JsonUtils.toJSONString(jsonBody)))
                .build();
        try {
            var httpResponse = httpClient.send(post, HttpResponse.BodyHandlers.ofString());
            return httpResponse.body();
        } catch (IOException | InterruptedException e) {
            throw new IllegalArgumentException(e);
        }
    }

    /**
     * 解析查询参数
     *
     * @param params 参数（可以是map也可以使普通对象）
     * @return 解析后的查询参数，形如：?id=1&name=hhh
     */
    @SuppressWarnings("unchecked")
    private static String parseQueryParam(Object params) {
        if (params == null) {
            return "";
        }
        Map<String, Object> paramMap;
        if (ObjectUtils.isMap(params)) {
            paramMap = (Map<String, Object>) params;
        } else {
            paramMap = BeanUtils.bean2Map(params);
        }

        StringBuilder sb = new StringBuilder("?");
        boolean isFirst = true;
        for (Map.Entry<String, Object> entry : paramMap.entrySet()) {
            String k = entry.getKey();
            Object v = entry.getValue();
            if (v != null) {
                if (!isFirst) {
                    sb.append("&");
                } else {
                    isFirst = false;
                }
                sb.append(k);
                sb.append("=");
                sb.append(v);
            }
        }
        return sb.toString();
    }
}
