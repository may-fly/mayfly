package mayfly.core.util.http;

import mayfly.core.util.Assert;
import mayfly.core.util.bean.BeanUtils;
import mayfly.core.util.JsonUtils;
import mayfly.core.util.ObjectUtils;
import okhttp3.FormBody;
import okhttp3.Headers;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;

import java.util.Map;
import java.util.Objects;

/**
 * @author meilin.huang
 * @version 1.0
 * @date 2019-12-04 3:55 下午
 */
public class HttpUtils {

    private static OkHttpClient client = new OkHttpClient();

    public static final MediaType JSON_TYPE = MediaType.get("application/json; charset=utf-8");


    public static String get(String url, Object params) throws Exception {
        return request(HttpRequest.create(url).method(HttpRequest.GET).queryParams(params));
    }

    /**
     * post 表单发送（CONTENT_TYPE = application/x-www-form-urlencoded）
     *
     * @param url  url，请求url
     * @param body 请求主题
     * @return 响应结果
     * @throws Exception 异常
     */
    public static String post(String url, Object body) throws Exception {
        return request(HttpRequest.create(url).method(HttpRequest.POST).body(body).contentType(HttpRequest.CONTENT_TYPE_WWW));
    }


    /**
     * post请求 （CONTENT_TYPE = application/json; charset=utf-8）
     *
     * @param url      url，请求url
     * @param jsonBody 请求主体
     * @return 响应结果
     * @throws Exception 异常
     */
    public static String postJson(String url, Object jsonBody) throws Exception {
        return request(HttpRequest.create(url).method(HttpRequest.POST).body(jsonBody).contentType(HttpRequest.CONTENT_TYPE_JSON));
    }

    /**
     * 发送http请求
     *
     * @param request http请求
     * @return 结果
     * @throws Exception 请求异常
     */
    public static String request(HttpRequest request) throws Exception {
        Request.Builder okhttpReqBuilder = new Request.Builder()
                .url(request.getUrl());
        // 请求头
        Map<String, String> headers = request.getHeaders();
        if (headers != null) {
            okhttpReqBuilder.headers(getHeaders(headers));
        }
        // 请求体处理
        okhttpReqBuilder.method(request.getMethod(), getRequestBody(request));
        return sendRequest(okhttpReqBuilder.build());
    }


    private static String sendRequest(Request request) throws Exception {
        try (Response response = client.newCall(request).execute()) {
            if (response.isSuccessful()) {
                ResponseBody body = response.body();
                Assert.notNull(body, "response body为空");
                return body.string();
            }
            throw new Exception("请求失败，响应码：" + response.code());
        }
    }

    @SuppressWarnings("unchecked")
    private static RequestBody getRequestBody(HttpRequest request) {
        Object requestBody = request.getBody();
        if (requestBody == null) {
            return null;
        }
        int contentType = request.getContentType();
        if (contentType == HttpRequest.CONTENT_TYPE_WWW) {
            FormBody.Builder builder = new FormBody.Builder();
            Map<String, Object> bodyMap;
            if (ObjectUtils.isMap(request)) {
                bodyMap = (Map<String, Object>) requestBody;
            } else {
                bodyMap = BeanUtils.bean2Map(requestBody);
            }
            bodyMap.forEach((k, v) -> {
                if (v != null) {
                    builder.add(k, Objects.toString(v));
                }
            });
            return builder.build();
        }
        if (contentType == HttpRequest.CONTENT_TYPE_JSON) {
            return RequestBody.create(JSON_TYPE, JsonUtils.toJSONString(requestBody));
        }
        return null;
    }

    private static Headers getHeaders(Map<String, String> headers) {
        Headers.Builder headerBuilder = new Headers.Builder();
        headers.forEach(headerBuilder::add);
        return headerBuilder.build();
    }
}
