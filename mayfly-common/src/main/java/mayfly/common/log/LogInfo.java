package mayfly.common.log;

import com.alibaba.fastjson.JSON;
import mayfly.common.utils.PlaceholderResolver;
import mayfly.common.utils.StringUtils;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author meilin.huang
 * @version 1.0
 * @description: 日志基本信息
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

    /**
     * 方法中无需记录日志的参数索引位置
     */
    private List<Integer> noNeedLogParamIndex;

    private LogInfo(String descAndInvoke) {
        this.descAndInvoke = descAndInvoke;
    }

    /**
     * 移除不需要记录日志的参数索引位置
     * @param allArgs
     * @return
     */
    public Object[] removeNoNeedLogArgs(Object[] allArgs) {
        //如果不需要记录日志的参数为空，则直接返回
        if (noNeedLogParamIndex.isEmpty()) {
            return allArgs;
        }
        Object[] needLogArgs = new Object[allArgs.length - noNeedLogParamIndex.size()];
        int needLogArgsIndex = 0;
        for (int i = 0; i < allArgs.length; i++) {
            if (noNeedLogParamIndex.contains(i)) {
                continue;
            }
            needLogArgs[needLogArgsIndex++] = allArgs[i];
        }
        return needLogArgs;
    }

    /**
     * 填充日志占位符
     * @param result  要记录的具体日志信息
     * @return
     */
    public String fillLogMsg(LogResult result) {
        StringBuilder logMsg = new StringBuilder(this.descAndInvoke);

        Map value = new HashMap(8);
        if (this.result) {
            logMsg.append(RESULT_MSG_TEMP);
            value.put("result", JSON.toJSONString(result.getResult()));
        }
        if (this.time) {
            logMsg.append(TIME_MSG_TEMP);
            value.put("time", result.getTime());
        }

        Object[] args = this.removeNoNeedLogArgs(result.getArgs());
        for (int i = 0; i < args.length; i++) {
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

        StringBuilder logMsg =  new StringBuilder(this.descAndInvoke).append(EXCEPTION_MSG_TEMP);
        Map value = new HashMap(4);
        value.put("e", result.getE().getMessage());

        Object[] args = this.removeNoNeedLogArgs(result.getArgs());
        for (int i = 0; i < args.length; i++) {
            value.put("param" + i, JSON.toJSONString(args[i]));
        }

        logMsg.append("\n -----------------------------------------------------------");
        return resolver.resolveByMap(logMsg.toString(), value);
    }

    public String getDescAndInvoke() {
        return this.descAndInvoke;
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

        public LogInfo build() {
            return logInfo;
        }
    }
}
