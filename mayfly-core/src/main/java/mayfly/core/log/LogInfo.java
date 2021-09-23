package mayfly.core.log;

import mayfly.core.log.annotation.Log;
import org.springframework.expression.Expression;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;

import java.lang.reflect.Method;
import java.util.Optional;

/**
 * 日志信息
 *
 * @author meilin.huang
 * @date 2021-09-17 7:40 下午
 */
public class LogInfo {

    /**
     * 解析spel表达式
     */
    private static final ExpressionParser parser = new SpelExpressionParser();

    /**
     * 日志信息
     */
    private String content;

    /**
     * 日志级别
     */
    private Log.Level level;

    /**
     * 记录返回值日志级别
     */
    private Log.Level resLevel;

    /**
     * 是否记录返回结果，默认true
     */
    private boolean logRes;

    /**
     * 使用spel表达式会有该值
     */
    private Expression expression;

    /**
     * 从方法获取对应的日志元信息
     *
     * @param method 方法
     * @return 日志元信息
     */
    public static LogInfo from(Method method) {
        Log ml = Optional.ofNullable(method.getAnnotation(Log.class))
                .orElse(method.getDeclaringClass().getAnnotation(Log.class));
        LogInfo li = new LogInfo();
        li.content = ml.value();
        li.level = ml.level();
        li.resLevel = ml.resLevel();
        li.logRes = ml.res();
        if (ml.el()) {
            try {
                li.expression = parser.parseExpression(li.content);
            } catch (Exception e) {
                // skip
            }
        }
        return li;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Log.Level getLevel() {
        return level;
    }

    public void setLevel(Log.Level level) {
        this.level = level;
    }

    public Log.Level getResLevel() {
        return resLevel;
    }

    public void setResLevel(Log.Level resLevel) {
        this.resLevel = resLevel;
    }

    public boolean isLogRes() {
        return logRes;
    }

    public void setLogRes(boolean logRes) {
        this.logRes = logRes;
    }

    public Expression getExpression() {
        return expression;
    }
}
