package mayfly.core.permission;

import mayfly.core.base.model.Result;
import mayfly.core.exception.BusinessException;
import mayfly.core.permission.registry.PermissionCheckHandler;
import mayfly.core.permission.registry.SimpleLoginAccountRegistry;
import mayfly.core.util.JsonUtils;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;
import java.util.Optional;

/**
 * 权限拦截器
 *
 * @author hml
 * @date 2018/6/14 下午3:23
 */
public class PermissionInterceptor implements HandlerInterceptor {

    private static final String DEFAULT_TOKEN_NAME = "token";

    /**
     * token参数名
     */
    private String tokenParamName = DEFAULT_TOKEN_NAME;

    private final PermissionCheckHandler checkHandler;

    public PermissionInterceptor() {
        this.checkHandler = PermissionCheckHandler.getDefaultHandler();
    }

    public PermissionInterceptor(SimpleLoginAccountRegistry loginAccountRegistry) {
        this.checkHandler = PermissionCheckHandler.of(loginAccountRegistry);
    }

    public PermissionInterceptor(String tokenParamName, SimpleLoginAccountRegistry loginAccountRegistry) {
        this.tokenParamName = tokenParamName;
        this.checkHandler = PermissionCheckHandler.of(loginAccountRegistry);
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        if (request.getMethod().equals(RequestMethod.OPTIONS.name())) {
            return true;
        }
        String token = Optional.ofNullable(request.getHeader(tokenParamName))
                .orElse(request.getParameter(tokenParamName));
        try {
            // 判断该用户是否有执行该方法的权限，如果校验通过，返回true
            if (!(handler instanceof HandlerMethod) || checkHandler.hasPermission(token, ((HandlerMethod) handler).getMethod())) {
                if (!"admin".equals(LoginAccount.getFromContext().getUsername()) && !"GET".equals(request.getMethod())) {
                    sendErrorMessage(response, Result.failure("只可观望"));
                    return false;
                }
                return true;
            }
            // token 过期
            return noPermission(response);
        } catch (BusinessException e) {
            //权限禁用or没有权限
            sendErrorMessage(response, Result.error(e.getErrorCode(), e.getMessage()));
            return false;
        }
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {
        // 移除ThreadLocal值
        LoginAccount.removeFromContext();
    }


    public static boolean noPermission(HttpServletResponse response) {
        sendErrorMessage(response, Result.withoutPermission());
        return false;
    }

    /**
     * 发送无权限消息
     *
     * @param response  response
     */
    public static void sendErrorMessage(HttpServletResponse response, Result<?> result) {
        response.setContentType("application/json; charset=utf-8");
        response.setHeader("Access-Control-Allow-Origin", "*");
        response.setHeader("Access-Control-Allow-Methods", "POST, GET, OPTIONS, DELETE, PUT");
        response.setHeader("Access-Control-Max-Age", "3600");
        try {
            PrintWriter writer = response.getWriter();
            writer.print(JsonUtils.toJSONString(result));
            writer.close();
            response.flushBuffer();
        } catch (Exception e) {
            //skip
        }
    }
}
