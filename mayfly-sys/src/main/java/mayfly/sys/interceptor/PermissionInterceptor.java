package mayfly.sys.interceptor;

import mayfly.common.result.Result;
import mayfly.common.utils.StringUtils;
import mayfly.common.web.PermissionHandler;
import mayfly.common.web.RequestUri;
import mayfly.common.web.UriPattern;
import mayfly.sys.service.PermissionService;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;

/**
 * 权限拦截器
 * @author: hml
 * @date: 2018/6/14 下午3:23
 */
public class PermissionInterceptor implements HandlerInterceptor {

    private static PermissionHandler permissionHandler = PermissionHandler.getInstance();

    private PermissionService permissionService;

    public PermissionInterceptor(PermissionService permissionService) {
        this.permissionService = permissionService;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        if (request.getMethod().equals(RequestMethod.OPTIONS.name())) {
            return true;
        }
        String token = request.getHeader("token");
        if (StringUtils.isEmpty(token)) {
            sendErrorMessage(response, Result.withoutPermission());
            return false;
        }
        if (permissionService.getIdByToken(token) == null) {
            sendErrorMessage(response, Result.withoutPermission());
            return false;
        }

        //校验用户权限uri中是否含有该uri匹配的uriPattern
        UriPattern pattern = permissionHandler.matchAndReturnPattern(new RequestUri(request.getMethod(), request.getRequestURI())
                , permissionService.getUriPermissionByToken(token));
        if (pattern == null){
            sendErrorMessage(response, Result.withoutPermission());
            return false;
        }

        return true;
    }

    /**
     * 发送无权限消息
     * @param response
     * @throws Exception
     */
    public static void sendErrorMessage(HttpServletResponse response, Result result){
        response.setContentType("application/json; charset=utf-8");
        response.setHeader("Access-Control-Allow-Origin", "*");
        response.setHeader("Access-Control-Allow-Methods", "POST, GET, OPTIONS, DELETE, PUT");
        response.setHeader("Access-Control-Max-Age", "3600");
//        response.setHeader("Access-Control-Allow-Headers", "x-requested-with");
        try {
            PrintWriter writer = response.getWriter();
            writer.print(result.toString());
            writer.close();
            response.flushBuffer();
        } catch (Exception e) {
            //skip
        }
    }
}
