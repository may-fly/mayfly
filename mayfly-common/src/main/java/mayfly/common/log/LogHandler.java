package mayfly.common.log;

import mayfly.common.util.annotation.AnnotationUtils;
import mayfly.common.util.CollectionUtils;
import mayfly.common.util.StringUtils;

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
     * 缓存日志的基本信息，key : 方法  value : LogInfo对象
     */
    private static final Map<Method, LogInfo> LOG_CACHE = new ConcurrentHashMap<>(128);

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
    public LogInfo getLogInfo(final Method method) {
        return LOG_CACHE.computeIfAbsent(method, this::parseLogMsg);
    }


    /**
     * 解析方法上对应的注解，生成对应的LogInfo对象
     * @param method
     * @return
     */
    private LogInfo parseLogMsg(Method method) {
        int argsCount = method.getParameterCount();
        // log描述
        String desc;
        MethodLog log = method.getAnnotation(MethodLog.class);
        if (log == null) {
            //如果方法没有该注解，则判断方法声明类上的该注解
            log = method.getDeclaringClass().getAnnotation(MethodLog.class);
            if (log == null) {
                throw new IllegalArgumentException(method + "方法或类必须添加@MethodLog注解！");
            }
            desc = StringUtils.isEmpty(log.value()) ? "" : log.value() + method.getName();
        } else {
            MethodLog typeLog = method.getDeclaringClass().getAnnotation(MethodLog.class);
            String typeMsg = typeLog != null ? typeLog.value() : "";
            desc = typeMsg + log.value();
        }

        //获取调用方法中不需要记录日志的参数索引位置
        List<Integer> noNeedLogParamIndex = null;
        Parameter[] params = method.getParameters();
        for (int i = 0; i < params.length; i++) {
            if (AnnotationUtils.isAnnotationPresent(params[i], NoNeedLogParam.class)) {
                if (noNeedLogParamIndex == null) {
                    noNeedLogParamIndex = new ArrayList<>(4);
                }
                noNeedLogParamIndex.add(i);
            }
        }

        //参数占位符
        StringBuilder paramPlaceholder = new StringBuilder();
        boolean first = true;
        //遍历所有参数，如果参数对应的所有存在于不需要记录的参数索引列表中，则跳过该参数，不生成对应的占位符
        for (int i = 0; i < argsCount; i++) {
            if (CollectionUtils.contains(noNeedLogParamIndex, i)) {
                continue;
            }
            if (first) {
                paramPlaceholder.append(params[i].getName()).append(":${param").append(i).append("}");
                first = false;
            } else {
                paramPlaceholder.append(", ").append(params[i].getName()).append(":${param").append(i).append("}");
            }
        }

        StringBuilder descAndInvoke = new StringBuilder();
        if (!StringUtils.isEmpty(desc)) {
            descAndInvoke.append("\n| description: ").append(desc);
        }
        //构建日志信息,方法参数前[]中的数字表示为参数的索引，即0：第一个参数；1：第二个参数
        descAndInvoke.append("\n| invoke: ").append(method.getDeclaringClass().getName() + "#" + method.getName())
                .append("(").append(paramPlaceholder).append(")");

        return LogInfo.builder(descAndInvoke.toString()).noNeedLogParamIndex(noNeedLogParamIndex)
                .time(log.time()).result(log.result()).resultLevel(log.resultLevel()).level(log.level()).build();
    }

}