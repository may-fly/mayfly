package mayfly.core.log;

import mayfly.core.util.Assert;
import mayfly.core.util.CollectionUtils;
import mayfly.core.util.PlaceholderResolver;
import mayfly.core.util.StringUtils;
import mayfly.core.util.annotation.AnnotationUtils;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 日志处理器
 *
 * @author meilin.huang
 * @version 1.0
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
     *
     * @param method  需要记录日志的方法
     * @return 日志信息，含有需要记录的基本信息
     */
    public LogInfo getLogInfo(final Method method) {
        return LOG_CACHE.computeIfAbsent(method, LogHandler::parseLogMsg);
    }


    /**
     * 解析方法上对应的注解，生成对应的LogInfo对象
     *
     * @param method   需要解析的方法
     * @return         {@link LogInfo}
     */
    private static LogInfo parseLogMsg(Method method) {
        // log描述
        String desc;
        MethodLog log = AnnotationUtils.getAnnotation(method, MethodLog.class);
        if (log == null) {
            //如果方法没有该注解，则判断方法声明类上的该注解
            log = AnnotationUtils.getAnnotation(method.getDeclaringClass(), MethodLog.class);
            Assert.notNull(log, "方法或类必须添加@MethodLog注解！");
            desc = StringUtils.isEmpty(log.value()) ? "" : log.value() + method.getName();
        } else {
            MethodLog typeLog = AnnotationUtils.getAnnotation(method.getDeclaringClass(), MethodLog.class);
            String typeMsg = typeLog != null ? typeLog.value() : "";
            desc = typeMsg + log.value();
        }

        Parameter[] params = method.getParameters();
        //获取调用方法中不需要记录日志的参数索引位置
        List<Integer> noNeedLogParamIndex = getNoNeedLogParams(params);
        StringBuilder descAndInvoke = new StringBuilder();
        if (!StringUtils.isEmpty(desc)) {
            descAndInvoke.append(desc);
        }
        //构建日志信息,方法参数前[]中的数字表示为参数的索引，即0：第一个参数；1：第二个参数
        descAndInvoke.append(LogInfo.INVOKER_MSG_PREFIX).append(method.getDeclaringClass().getName()).append("#")
                .append(method.getName()).append("(")
                .append(getParamsPlaceholder(params, noNeedLogParamIndex))
                .append(")");

        LogInfo info = new LogInfo(descAndInvoke.toString());
        info.noNeedLogParamIndex = noNeedLogParamIndex;
        info.time = log.time();
        info.result = method.getReturnType() != Void.TYPE && log.result();
        info.resultLevel = log.resultLevel();
        info.level = log.level();
        return info;
    }

    /**
     * 获取不需要记录日志的参数的索引位置
     *
     * @param params  参数数组
     * @return         无不需要记录的参数，则返回null
     */
    private static List<Integer> getNoNeedLogParams(Parameter[] params) {
        List<Integer> noNeedLogParamIndex = null;
        for (int i = 0; i < params.length; i++) {
            if (params[i].isAnnotationPresent(NoNeedLogParam.class)) {
                if (noNeedLogParamIndex == null) {
                    noNeedLogParamIndex = new ArrayList<>(4);
                }
                noNeedLogParamIndex.add(i);
            }
        }
        return noNeedLogParamIndex;
    }

    /**
     * 获取参数占位符字符串
     *
     * @param params   参数数组
     * @param noNeedLogParamIndex  不需要记录日志的参数索引
     * @return      占位符
     */
    private static String getParamsPlaceholder(Parameter[] params, List<Integer> noNeedLogParamIndex) {
        //参数占位符
        StringBuilder paramPlaceholder = new StringBuilder();
        boolean first = true;
        //遍历所有参数，如果参数对应的索引存在于不需要记录的参数索引列表中，则跳过该参数，不生成对应的占位符
        for (int i = 0; i < params.length; i++) {
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
        return paramPlaceholder.toString();
    }



    /**
     * 日志基本信息
     *
     * @author meilin.huang
     * @version 1.0
     * @date 2018-11-12 9:18 AM
     */
    public static class LogInfo {

        /**
         * 调用信息前缀
         */
        public static final String INVOKER_MSG_PREFIX = "\n==> Invoke: ";

        /**
         * 返回结果模板
         */
        public static final String RESULT_MSG_TEMP = "\n<== Result: ${result}";

        /**
         * 执行时间模板
         */
        public static final String TIME_MSG_TEMP = " -> (ExeTime: ${time})";

        /**
         * 异常信息模板
         */
        public static final String EXCEPTION_MSG_TEMP = "\n<== Exception: ${e}";

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
         * 指定需要打印日志的级别
         */
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
         *
         * @param sysLogLevel 系统日志级别
         * @param result 要记录的具体日志信息
         * @return 如果打印日志的级别小于系统日志级别，则返回null,即无需记录该日志
         */
        public String fillLogMsg(MethodLog.LogLevel sysLogLevel, LogResult result) {
            // 方法的日志级别<系统日志级别直接返回
            if (level.order() < sysLogLevel.order()) {
                return null;
            }
            StringBuilder logMsg = new StringBuilder();
            logMsg.append(this.descAndInvoke);
            Map<String, Object> value = new HashMap<>(8);
            // 是否记录执行时间
            if (this.time) {
                logMsg.append(TIME_MSG_TEMP);
                value.put("time", result.time);
            }
            // 当记录日志结果属性为true并且记录结果的级别大于系统级别时填充结果属性
            if (this.result && this.resultLevel.order() >= sysLogLevel.order()) {
                logMsg.append(RESULT_MSG_TEMP);
                value.put("result", Objects.toString(result.result));
            }

            Object[] args = result.args;
            for (int i = 0; i < args.length; i++) {
                if (CollectionUtils.contains(noNeedLogParamIndex, i)) {
                    continue;
                }
                value.put("param" + i, Objects.toString(args[i]));
            }

            return resolver.resolveByMap(logMsg.toString(), value);
        }

        /**
         * 获取异常日志信息
         *
         * @param result  日志
         * @return         日志信息
         */
        public String getExceptionLogMsg(LogResult result) {
            if (result.e == null) {
                throw new RuntimeException("LogResult中异常对象字段不能为空");
            }

            StringBuilder logMsg = new StringBuilder();
            logMsg.append(this.descAndInvoke).append(EXCEPTION_MSG_TEMP);
            Map<String, Object> value = new HashMap<>(4);
            value.put("e", result.e.getMessage());

            Object[] args = result.args;
            for (int i = 0; i < args.length; i++) {
                if (CollectionUtils.contains(noNeedLogParamIndex, i)) {
                    continue;
                }
                value.put("param" + i, Objects.toString(args[i]));
            }

            return resolver.resolveByMap(logMsg.toString(), value);
        }

        /**
         * 获取当前日志信息的级别
         *
         * @return loglevel
         */
        public MethodLog.LogLevel getLevel() {
            return this.level;
        }
    }


    /**
     * @author meilin.huang
     * @version 1.0
     * @date 2018-11-19 3:27 PM
     */
    public static class LogResult {

        /**
         * 参数列表
         */
        private Object[] args;

        /**
         * 结果
         */
        private Object result;

        /**
         * 执行时间
         */
        private long time;

        /**
         * 异常信息
         */
        private Exception e;

        /**
         * 构造函数
         *
         * @param args    参数数组
         * @param result  结果
         * @param time    执行时间
         */
        public LogResult(Object[] args, Object result, long time) {
            this.args = args;
            this.result = result;
            this.time = time;
        }

        /**
         * 构造函数
         *
         * @param args  参数
         * @param e     异常
         */
        public LogResult(Object[] args, Exception e) {
            this.args = args;
            this.e = e;
        }

        /**
         * 返回异常对象结果
         *
         * @param args  参数列表
         * @param e     异常
         * @return      result
         */
        public static LogResult exception(Object[] args, Exception e) {
            return new LogResult(args, e);
        }
    }

}