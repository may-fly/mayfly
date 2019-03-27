package mayfly.common.web;

import java.util.Collection;

/**
 * uri匹配处理器
 * @author: hml
 * @date: 2018/6/14 上午8:52
 */
public class UriMatchHandler {

    private UriMatchHandler(){}

    private static UriMatchHandler handler = new UriMatchHandler();

    public static UriMatchHandler getInstance() {
        return handler;
    }

    public boolean match(RequestUri requestUri, Collection<UriPattern> uris) {
        for (UriPattern up : uris) {
            if (up.match(requestUri) != null) {
                return true;
            }
        }
        return false;
    }

    public UriPattern.MatchResult matchAndReturnResult(RequestUri requestUri, Collection<UriPattern> uriPatterns) {
        for (UriPattern up : uriPatterns) {
            UriPattern.MatchResult result = up.match(requestUri);
            if (result != null) {
                return result;
            }
        }
        return null;
    }

    public UriPattern matchAndReturnPattern(RequestUri requestUri, Collection<UriPattern> uriPatterns) {
        UriPattern.MatchResult result = matchAndReturnResult(requestUri, uriPatterns);
        return result != null ? result.getUriPattern() : null;
    }
}
