package mayfly.common.web;

import java.util.HashMap;
import java.util.Map;

/**
 * uri模式匹配
 * @author: meilin.huang
 * @date: 2018/6/14 上午9:59
 */
public class UriPattern {
    /**
     * restful url占位符模式
     */
    public static final String REST_FLAG_PATTERN = "\\{\\w*\\}";

    /**
     * 请求方法
     */
    private RequestMethod method;

    /**
     * uri模式
     */
    private String uriPattern;

    /**
     * 是否为简单模式，简单模式即不包含restful风格以及带有正则规则
     */
    private boolean simple = false;

    public UriPattern(){}

    public UriPattern(RequestMethod method, String uriPattern) {
        this.method = method;
        this.uriPattern = uriPattern;
        if (!uriPattern.contains("{")) {
            this.simple = true;
        }
    }


    /**
     * 匹配具体uri对象
     * @param requestUri
     * @return
     */
    public MatchResult match(RequestUri requestUri) {
        if (!this.method.equals(requestUri.getMethod())) {
            return null;
        }
        if (simple) {
            return requestUri.getUri().equals(this.getUriPattern()) ? new MatchResult(requestUri, this) : null;
        }

        return doPatternMatch(requestUri);
    }

    private MatchResult doPatternMatch(RequestUri requestUri) {
        String[] rUris = requestUri.getUri().split("/");
        String[] pUris = this.getUriPattern().split("/");
        if (rUris.length != pUris.length) {
            return null;
        }

        Map<String, String> uriParam = new HashMap<>(8);
        for (int i = 0; i < rUris.length; i++) {
            String r = rUris[i];
            String p = pUris[i];
            if (r.equals(p)) {
                continue;
            }

            if (!p.matches(REST_FLAG_PATTERN)) {
                return null;
            }
            uriParam.put(p.substring(1, p.length() - 1), r);
        }
        return new MatchResult(requestUri, this, uriParam);
    }

    /**
     * 匹配结果类
     */
    static class MatchResult {
        private RequestUri requestUri;

        private UriPattern uriPattern;

        /**
         * restful风格占位符
         */
        private Map<String, String> uriParam;

        public MatchResult(RequestUri requestUri, UriPattern uriPattern) {
            this.requestUri = requestUri;
            this.uriPattern = uriPattern;
        }

        public MatchResult(RequestUri requestUri, UriPattern uriPattern, Map<String, String> uriParam) {
            this.requestUri = requestUri;
            this.uriPattern = uriPattern;
            this.uriParam = uriParam;
        }

        public Map<String, String> getUriParam() {
            return this.uriParam;
        }

        public UriPattern getUriPattern() {
            return this.uriPattern;
        }

        public RequestUri getRequestUri() {
            return this.requestUri;
        }
    }



    /* getter setter*/

    public RequestMethod getMethod() {
        return method;
    }

    public void setMethod(RequestMethod method) {
        this.method = method;
    }

    public String getUriPattern() {
        return uriPattern;
    }

    public void setUriPattern(String uriPattern) {
        this.uriPattern = uriPattern;
    }

    public boolean isSimple() {
        return simple;
    }

    public void setSimple(boolean simple) {
        this.simple = simple;
    }
}
