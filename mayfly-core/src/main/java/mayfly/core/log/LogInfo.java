package mayfly.core.log;

import com.alibaba.fastjson.JSON;
import mayfly.core.util.CollectionUtils;
import mayfly.core.util.PlaceholderResolver;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 日志基本信息
 * @author meilin.huang
 * @version 1.0
 * @date 2018-11-12 9:18 AM
 */
public class LogInfo {

    public static final String RESULT_MSG_TEMP = "\n| return: ${result}";

    public static final String TIME_MSG_TEMP = "\n| executeTime: ${time}";

    public static final String EXCEPTION_MSG_TEMP = "\n| exception: ${e}";

    /**
     * 占位符解析器
     */
    private static PlaceholderResolver resolver = PlaceholderResolver.getDefaultResolver();

    /**
     * 是否记录返回结果
     */
    private boolean result;

    /**
     * 是否记录执行时间
     */
    private boolean time;

    /**
     * 方法描述与调用路径
     */
    private String descAndInvoke;

    private MethodLog.LogLevel level;

    /**
     * 结果记录级别
     */
    private MethodLog.LogLevel resultLevel;

    /**
     * 方法中无需记录日志的参数索引位置
     */
    private List<Integer> noNeedLogParamIndex;

    private LogInfo(String descAndInvoke) {
        this.descAndInvoke = descAndInvoke;
    }

    /**
     * 填充日志占位符
     * @param result  要记录的具体日志信息
     * @return 如果打印日志的级别小于系统日志级别，则返回null,即无需记录该日志
     */
    public String fillLogMsg(MethodLog.LogLevel sysLogLevel, LogResult result) {
        // 方法的日志级别<系统日志级别直接返回
        if (level.order() < sysLogLevel.order()) {
            return null;
        }
        StringBuilder logMsg = new StringBuilder("\n -----------------------------------------------------------");
        logMsg.append(this.descAndInvoke);
        Map<String, Object> value = new HashMap<>(8);
        // 当记录日志结果属性为true并且记录结果的级别大于系统级别时填充结果属性
        if (this.result && this.resultLevel.order() >= sysLogLevel.order()) {
            logMsg.append(RESULT_MSG_TEMP);
            value.put("result", JSON.toJSONString(result.getResult()));
        }
        if (this.time) {
            logMsg.append(TIME_MSG_TEMP);
            value.put("time", result.getTime());
        }

        Object[] args = result.getArgs();
        for (int i = 0; i < args.length; i++) {
            if (CollectionUtils.contains(noNeedLogParamIndex, i)) {
                continue;
            }
            value.put("param" + i, JSON.toJSONString(args[i]));
        }

        logMsg.append("\n -----------------------------------------------------------");
        return resolver.resolveByMap(logMsg.toString(), value);
    }

    /**
     * 获取异常日志信息
     * @param result
     * @return
     */
    public String getExceptionLogMsg(LogResult result) {
        if (result.getE() == null) {
            throw new RuntimeException("LogResult中异常对象字段不能为空");
        }

        StringBuilder logMsg =  new StringBuilder("\n -----------------------------------------------------------");
        logMsg.append(this.descAndInvoke).append(EXCEPTION_MSG_TEMP);
        Map<String, Object> value = new HashMap<>(4);
        value.put("e", result.getE().getMessage());

        Object[] args = result.getArgs();
        for (int i = 0; i < args.length; i++) {
            if (CollectionUtils.contains(noNeedLogParamIndex, i))  {
                continue;
            }
            value.put("param" + i, JSON.toJSONString(args[i]));
        }

        logMsg.append("\n -----------------------------------------------------------");
        return resolver.resolveByMap(logMsg.toString(), value);
    }

    public String getDescAndInvoke() {
        return this.descAndInvoke;
    }

    public MethodLog.LogLevel getLevel() {
        return this.level;
    }


    public static Builder builder(String descAndInvoke) {
        return new Builder(descAndInvoke);
    }

    public static class Builder{
        private LogInfo logInfo;

        public Builder(String descAndInvoke) {
            this.logInfo = new LogInfo(descAndInvoke);
        }

        public Builder noNeedLogParamIndex(List<Integer> index) {
            logInfo.noNeedLogParamIndex = index;
            return this;
        }

        public Builder result(boolean result) {
            logInfo.result = result;
            return this;
        }

        public Builder time(boolean time) {
            logInfo.time = time;
            return this;
        }

        public Builder level(MethodLog.LogLevel level) {
            logInfo.level = level;
            return this;
        }

        public Builder resultLevel(MethodLog.LogLevel resultLevel) {
            logInfo.resultLevel = resultLevel;
            return this;
        }

        public LogInfo build() {
            return logInfo;
        }
    }
}
