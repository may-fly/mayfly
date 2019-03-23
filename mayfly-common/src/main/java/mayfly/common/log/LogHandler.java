package mayfly.common.log;

import mayfly.common.utils.StringUtils;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author meilin.huang
 * @version 1.0
 * @description: 日志处理器
 * @date 2018-11-09 11:18 AM
 */
public class LogHandler {

    /**
     * 缓存日志的基本信息，key : 方法全限定名  value : LogInfo对象
     */
    private static final Map<String, LogInfo> LOG_CACHE = new ConcurrentHashMap(128);

    private LogHandler() {
    }

    private static LogHandler instance = new LogHandler();

    public static LogHandler getInstance() {
        return instance;
    }


    /**
     * 获取含有@MethodLog注解方法对应的日志信息
     * @param method
     * @return 日志信息，含有需要记录的基本信息
     */
    public LogInfo getLogInfo(Method method) {
        String invoke = method.getDeclaringClass().getName() + "." + method.getName();
        return LOG_CACHE.computeIfAbsent(invoke, key -> parseLogMsg(method));
    }


    /**
     * 解析方法上对应的注解，生成对应的LogInfo对象
     * @param method
     * @return
     */
    private LogInfo parseLogMsg(Method method) {
        // 方法调用全路径名
        String invoke = method.getDeclaringClass().getName() + "." + method.getName();
        int argsCount = method.getParameterCount();

        MethodLog log = method.getAnnotation(MethodLog.class);
        if (log == null) {
            throw new IllegalArgumentException(invoke + "方法必须添加@MethodLog注解！");
        }

        //获取调用方法中不需要记录日志的参数索引位置
        List<Integer> noNeedLogParamIndex = new ArrayList<>(8);
        Parameter[] params = method.getParameters();
        for (int i = 0; i < params.length; i++) {
            if (params[i].isAnnotationPresent(NoNeedLogParam.class)) {
                noNeedLogParamIndex.add(i);
            }
        }

        //参数占位符
        String paramPlaceholder = "";
        if (noNeedLogParamIndex.isEmpty()) {
            //如果没有不需要记录的参数则直接根据参数个数生成对应个数的占位符
            StringBuilder builder = new StringBuilder();
            for (int i = 0; i < argsCount; i++) {
                if (i == 0) {
                    builder.append("[").append(i).append("]:${").append("param").append(i).append("}");
                } else {
                    builder.append(", [").append(i).append("]:${").append("param").append(i).append("}");
                }
            }
            paramPlaceholder = builder.toString();
        } else {
            int needLogIndex = 0;
            StringBuilder paramPlaceholderBuilder = new StringBuilder();
            //遍历所有参数，如果参数对应的所有存在于不需要记录的参数索引列表中，则跳过该参数，不生成对应的占位符
            for (int i = 0; i < argsCount; i++) {
                if (noNeedLogParamIndex.contains(i)) {
                    continue;
                }
                if (needLogIndex == 0) {
                    paramPlaceholderBuilder.append("[" + i + "]:${").append("param").append(needLogIndex++).append("}");
                } else {
                    paramPlaceholderBuilder.append(", ");
                    paramPlaceholderBuilder.append("[" + i + "]:${").append("param").append(needLogIndex++).append("}");
                }
            }
            paramPlaceholder = paramPlaceholderBuilder.toString();
        }

        StringBuilder descAndInvoke = new StringBuilder("\n -----------------------------------------------------------")
                .append("\n| description: ");
        String desc = log.value();
        if (!StringUtils.isEmpty(desc)) {
            descAndInvoke.append(desc);
        } else {
            descAndInvoke.append(method.getName());
        }
        //构建日志信息,方法参数前[]中的数字表示为参数的索引，即0：第一个参数；1：第二个参数
        descAndInvoke.append("\n| invoke: ")
                .append(invoke).append("(").append(paramPlaceholder).append(")");

        LogInfo logInfo = LogInfo.builder(descAndInvoke.toString()).noNeedLogParamIndex(noNeedLogParamIndex)
                .time(log.time()).result(log.result()).build();
        return logInfo;
    }

}