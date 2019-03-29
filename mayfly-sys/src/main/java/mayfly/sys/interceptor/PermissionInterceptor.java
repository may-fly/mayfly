package mayfly.sys.interceptor;

import mayfly.common.permission.check.PermissionCheckHandler;
import mayfly.common.permission.PermissionDisabledException;
import mayfly.common.result.Result;
import mayfly.common.utils.StringUtils;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.method.HandlerMethod;
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

    private PermissionCheckHandlerService checkHandlerService;

    private PermissionCheckHandler checkHandler;

    public PermissionInterceptor(PermissionCheckHandlerService checkHandlerService) {
        this.checkHandlerService = checkHandlerService;
        this.checkHandler = checkHandlerService.getCheckHandler();
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
        Integer userId;
        if ((userId = checkHandlerService.getIdByToken(token)) == null) {
            sendErrorMessage(response, Result.withoutPermission());
            return false;
        }
        // 判断该用户是否有执行该方法的权限
        try {
            if (handler instanceof HandlerMethod && checkHandler.hasPermission(userId, ((HandlerMethod)handler).getMethod())) {
                return true;
            }
        } catch (PermissionDisabledException e) {
            sendErrorMessage(response, Result.error(e.getMessage()));
            return false;
        }

        sendErrorMessage(response, Result.withoutPermission());
        return false;
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
