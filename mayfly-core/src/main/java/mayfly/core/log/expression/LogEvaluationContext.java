package mayfly.core.log.expression;

import mayfly.core.log.InvokeLog;
import mayfly.core.log.LogContext;
import org.springframework.context.expression.MethodBasedEvaluationContext;
import org.springframework.core.LocalVariableTableParameterNameDiscoverer;
import org.springframework.core.ParameterNameDiscoverer;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Map;

/**
 * @author meilin.huang
 * @date 2021-09-23 2:51 下午
 */
public class LogEvaluationContext extends MethodBasedEvaluationContext {

    /**
     * 将方法参数纳入Spring管理
     */
    private static final LocalVariableTableParameterNameDiscoverer discoverer = new LocalVariableTableParameterNameDiscoverer();

    public LogEvaluationContext(Object rootObject, Method method, Object[] arguments,
                                ParameterNameDiscoverer parameterNameDiscoverer, Object ret) {
        //把方法的参数都放到 SpEL 解析的 RootObject 中
        super(rootObject, method, arguments, parameterNameDiscoverer);
        //把 LogContext 中的变量都放到 RootObject 中
        Map<String, Object> variables = LogContext.getVars();
        if (variables != null && variables.size() > 0) {
            for (Map.Entry<String, Object> entry : variables.entrySet()) {
                setVariable(entry.getKey(), entry.getValue());
            }
        }
        //把方法的返回值放到 RootObject 中
        setVariable("res", ret);
    }

    public static LogEvaluationContext from(InvokeLog invokeLog) {
        Object[] args = Arrays.stream(invokeLog.getArgs()).map(InvokeLog.Arg::getValue).toArray();
        return new LogEvaluationContext(args[0], invokeLog.getMethod(), args, discoverer, invokeLog.getRes());
    }
}
