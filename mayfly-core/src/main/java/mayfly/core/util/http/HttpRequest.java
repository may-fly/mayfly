package mayfly.core.util.http;

import mayfly.core.util.Assert;
import mayfly.core.util.bean.BeanUtils;
import mayfly.core.util.JsonUtils;
import mayfly.core.util.ObjectUtils;

import java.util.Map;

/**
 * @author meilin.huang
 * @version 1.0
 * @date 2019-12-04 5:56 下午
 */
public class HttpRequest {

    public static final String GET = "GET";
    public static final String POST = "POST";
    public static final String PUT = "PUT";
    public static final String DELETE = "DELETE";
    public static final String PATCH = "PATCH";

    /**
     * CONTENT_TYPE = application/json; charset=utf-8
     */
    public static final int CONTENT_TYPE_JSON = 1;
    /**
     * CONTENT_TYPE = application/x-www-form-urlencoded
     */
    public static final int CONTENT_TYPE_WWW = 2;


    private String url;

    /**
     * 请求主体，既可以是对象也可以是map
     */
    private Object body;

    /**
     * 请求 content-type
     */
    private int contentType;

    /**
     * 请求方法
     */
    private String method;

    /**
     * headers
     */
    private Map<String, String> headers;

    private HttpRequest(String url) {
        this.url = url;
    }

    public static HttpRequest create(String url) {
        Assert.notEmpty(url, "url不能为空");
        return new HttpRequest(url);
    }

    /**
     * get请求
     *
     * @return 请求结果
     * @throws Exception exception
     */
    public String get() throws Exception {
        this.method = GET;
        return HttpUtils.request(this);
    }

    /**
     * 将get请求取得的响应体转为指定类型对象
     *
     * @param type class
     * @param <T>  实际类型
     * @return 对象
     * @throws Exception exception
     */
    public <T> T getJsonObject(Class<T> type) throws Exception {
        return JsonUtils.parseObject(get(), type);
    }

    /**
     * 使用CONTENT_TYPE = application/x-www-form-urlencoded 发送post请求
     *
     * @return 响应结果
     * @throws Exception exception
     */
    public String post() throws Exception {
        this.method = POST;
        this.contentType = CONTENT_TYPE_WWW;
        return HttpUtils.request(this);
    }

    /**
     * 使用CONTENT_TYPE = application/json; charset=utf-8 发送post请求
     *
     * @return 响应结果
     * @throws Exception exception
     */
    public String postJson() throws Exception {
        this.method = POST;
        this.contentType = CONTENT_TYPE_JSON;
        return HttpUtils.request(this);
    }


    /***  链式调用属性赋值  ***/

    public HttpRequest method(String method) {
        this.method = method;
        return this;
    }

    /**
     * 查询参数，既可以是对象也可以是map, <br/>
     * 如果有查询参数，则直接在url处拼接
     *
     * @param param 查询参数
     * @return builder
     */
    @SuppressWarnings("unchecked")
    public HttpRequest queryParams(Object param) {
        if (param == null) {
            return this;
        }
        Map<String, Object> paramMap;
        if (ObjectUtils.isMap(param)) {
            paramMap = (Map<String, Object>) param;
        } else {
            paramMap = BeanUtils.bean2Map(param);
        }
        this.url = this.url + parseQueryParams(paramMap);
        return this;
    }

    public HttpRequest body(Object body) {
        this.body = body;
        return this;
    }

    public HttpRequest headers(Map<String, String> headers) {
        this.headers = headers;
        return this;
    }

    public HttpRequest contentType(int contentType) {
        this.contentType = contentType;
        return this;
    }

    public String getUrl() {
        return url;
    }

    public String getMethod() {
        return method;
    }

    public Map<String, String> getHeaders() {
        return headers;
    }

    public Object getBody() {
        return body;
    }

    public int getContentType() {
        return contentType;
    }


    private static String parseQueryParams(Map<String, Object> params) {
        StringBuilder sb = new StringBuilder("?");
        boolean isFirst = true;
        for (Map.Entry<String, Object> entry : params.entrySet()) {
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
