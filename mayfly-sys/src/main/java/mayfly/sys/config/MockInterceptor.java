package mayfly.sys.config;

import mayfly.core.exception.BizException;
import mayfly.core.util.HttpUtils;
import mayfly.core.util.JsonUtils;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;
import java.lang.reflect.Method;

/**
 * 权限拦截器
 *
 * @author hml
 * @date 2018/6/14 下午3:23
 */
public class MockInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        if (request.getMethod().equals(RequestMethod.OPTIONS.name())) {
            return true;
        }
        try {
            // 判断该用户是否有执行该方法的权限，如果校验通过，返回true
            if (handler instanceof HandlerMethod) {
                Method method = ((HandlerMethod) handler).getMethod();
                String s = HttpUtils.get("http://localhost:8888/api/mock-datas/" + method.getName());
                sendMessage(response, JsonUtils.parse(s).get("data"));
                return false;
            }
            return true;
        } catch (BizException e) {
            return true;
        }
    }

    /**
     * 发送无权限消息
     *
     * @param response response
     */
    public static void sendMessage(HttpServletResponse response, Object mockData) {
        response.setContentType("application/json; charset=utf-8");
        response.setHeader("Access-Control-Allow-Origin", "*");
        response.setHeader("Access-Control-Allow-Methods", "POST, GET, OPTIONS, DELETE, PUT");
        response.setHeader("Access-Control-Max-Age", "3600");
        try {
            PrintWriter writer = response.getWriter();
            writer.print(mockData);
            writer.close();
            response.flushBuffer();
        } catch (Exception e) {
            //skip
        }
    }
}
