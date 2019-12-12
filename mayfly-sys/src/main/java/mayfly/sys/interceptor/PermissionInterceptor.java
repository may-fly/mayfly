package mayfly.sys.interceptor;

import mayfly.core.permission.SessionLocal;
import mayfly.core.permission.checker.PermissionCheckHandler;
import mayfly.core.permission.PermissionDisabledException;
import mayfly.core.result.Result;
import mayfly.core.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;

/**
 * 权限拦截器
 *
 * @author hml
 * @date 2018/6/14 下午3:23
 */
public class PermissionInterceptor implements HandlerInterceptor {

    private PermissionCheckHandlerService checkHandlerService;

    private PermissionCheckHandler<Integer> checkHandler;

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
            return noPermission(response);
        }
        Integer userId = checkHandlerService.getIdByToken(token);
        if (userId == null) {
            return noPermission(response);
        }
        SessionLocal.setUserId(userId);
//        if (userId.equals(1)) {
//            return true;
//        }
        // 判断该用户是否有执行该方法的权限
        try {
            //如果校验通过，返回true
            if (!(handler instanceof HandlerMethod) || checkHandler.hasPermission(userId, ((HandlerMethod) handler).getMethod())) {
                return true;
            }
            // 无权限
            return noPermission(response);
        } catch (PermissionDisabledException e) {
            //权限禁用
            sendErrorMessage(response, Result.error(e.getMessage()));
            return false;
        }
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        // 移除ThreadLocal值
        SessionLocal.remove();
    }


    public static boolean noPermission(HttpServletResponse response) {
        sendErrorMessage(response, Result.withoutPermission());
        return false;
    }

    /**
     * 发送无权限消息
     *
     * @param response
     * @throws Exception
     */
    public static void sendErrorMessage(HttpServletResponse response, Result result) {
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
