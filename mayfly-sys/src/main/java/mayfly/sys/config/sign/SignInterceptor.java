package mayfly.sys.config.sign;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import mayfly.core.exception.BizAssert;
import mayfly.core.util.DigestUtils;
import mayfly.sys.common.utils.SignUtils;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.util.ContentCachingRequestWrapper;


/**
 * @author meilin.huang
 * @date 2020-06-28 5:10 下午
 */
public class SignInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String method = request.getMethod();
        if (method.equals(RequestMethod.OPTIONS.name())) {
            return true;
        }
        // 判断该用户是否有执行该方法的权限，如果校验通过，返回true
        NeedSign needSign;
        if (!(handler instanceof HandlerMethod) || (needSign = ((HandlerMethod) handler).getMethodAnnotation(NeedSign.class)) == null) {
            return true;
        }
        String signStr = request.getHeader("sign");
        BizAssert.notEmpty(signStr, "sign error");

        String signContent;
        if (method.equals(RequestMethod.POST.name()) || method.equals(RequestMethod.PUT.name())) {
            signContent = new String(((ContentCachingRequestWrapper)request).getContentAsByteArray());
//            signContent = IOUtils.read(request.getInputStream());
        } else {
            signContent = SignUtils.concatSignString(request, false, "sign");
        }
        BizAssert.equals(signStr, DigestUtils.md5DigestAsHex(signContent + "1111"), "sign error");
        return true;
    }
}
