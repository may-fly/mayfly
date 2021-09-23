package mayfly.core.log;

import mayfly.core.util.MapUtils;

import java.util.Map;

/**
 * @author meilin.huang
 * @date 2021-09-17 9:27 上午
 */
public final class LogContext {

    private static final InheritableThreadLocal<Map<String, Object>> VAR_MAP = new InheritableThreadLocal<>();

    public static final String OLD_OBJ_KEY = "oldObj";

    public static final String NEW_OBJ_KEY = "newObj";

    public static final String DEFAULT_LOG_MSG = "defaultLogMsg";

    /**
     * 设置日志上下文参数
     *
     * @param key 参数key
     * @param obj 参数值
     */
    public static void putVar(String key, Object obj) {
        if (VAR_MAP.get() == null) {
            VAR_MAP.set(MapUtils.<String, Object>hashMapBuilder(8).put(key, obj).build());
        } else {
            VAR_MAP.get().put(key, obj);
        }
    }

    /**
     * 获取日志上下文参数
     *
     * @param key 参数key
     * @return 参数值
     */
    public static Object getVar(String key) {
        if (VAR_MAP.get() == null) {
            return null;
        }
        return VAR_MAP.get().get(key);
    }

    /**
     * 设置旧值
     *
     * @param oldObj old obj
     */
    public static void setOldObj(Object oldObj) {
        putVar(OLD_OBJ_KEY, oldObj);
    }

    /**
     * 设置新值
     *
     * @param newObj new obj
     */
    public static void setNewObj(Object newObj) {
        putVar(NEW_OBJ_KEY, newObj);
    }

    public static Map<String, Object> getVars() {
        return VAR_MAP.get();
    }

    /**
     * 获取默认的日志信息内容
     *
     * @return 日志信息内容
     */
    public static String getDefaultLogMsg() {
        return String.valueOf(getVar(DEFAULT_LOG_MSG));
    }

    /**
     * 清空线程上下文值
     */
    public static void clear() {
        VAR_MAP.remove();
    }
}
