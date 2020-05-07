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

    private static final HttpClient HTTP_CLIENT = HttpClient.newHttpClient();

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
            var httpResponse = HTTP_CLIENT.send(httpGet, HttpResponse.BodyHandlers.ofString());
            return httpResponse.body();
        } catch (IOException | InterruptedException e) {
            throw new IllegalArgumentException(e);
        }
    }

    /**
     * 发送get请求获取json并转换为对应对象
     *
     * @param url  url
     * @param type response结果类型
     * @return response
     */
    public static <T> T get(String url, Class<T> type) {
        return JsonUtils.parseObject(get(url), type);
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
     * 发送带有查询参数的get请求获取json并转换为对应对象
     *
     * @param url    url
     * @param params 参数（可以是map也可以使普通对象）
     * @param type   response结果类型
     * @return response
     */
    public static <T> T get(String url, Object params, Class<T> type) {
        return JsonUtils.parseObject(get(url, params), type);
    }

    /**
     * 用Content-Type=application/json形式发送post请求
     *
     * @param url  url
     * @param json json字符串
     * @return response
     */
    public static String postJson(String url, String json) {
        var post = HttpRequest.newBuilder()
                .header("Content-Type", "application/json")
                .uri(URI.create(url))
                .POST(HttpRequest.BodyPublishers.ofString(json))
                .build();
        try {
            var httpResponse = HTTP_CLIENT.send(post, HttpResponse.BodyHandlers.ofString());
            return httpResponse.body();
        } catch (IOException | InterruptedException e) {
            throw new IllegalArgumentException(e);
        }
    }

    /**
     * 用Content-Type=application/json形式发送post请求
     *
     * @param url      url
     * @param jsonBody 请求体（对象）
     * @return response
     */
    public static String postJson(String url, Object jsonBody) {
        return postJson(url, JsonUtils.toJSONString(jsonBody));
    }

    /**
     * 用Content-Type=application/json形式发送post请求，并解析响应结果json对象
     *
     * @param url      url
     * @param jsonBody 请求体
     * @return response
     */
    public static <T> T postJson(String url, Object jsonBody, Class<T> type) {
        return JsonUtils.parseObject(postJson(url, jsonBody), type);
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

        var sb = new StringBuilder("?");
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
