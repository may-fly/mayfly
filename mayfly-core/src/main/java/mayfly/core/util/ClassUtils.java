package mayfly.core.util;

import org.springframework.lang.Nullable;

/**
 * @author meilin.huang
 * @version 1.0
 * @date 2020-01-14 10:26 上午
 */
public class ClassUtils {

    /**
     * 判断类名是否存在在classpath路径下
     *
     * @param className   类名
     * @param classLoader class loader
     * @return 是否存在
     */
    public static boolean isPresent(String className, ClassLoader classLoader) {
        try {
            Class.forName(className, false, classLoader);
            return true;
        } catch (IllegalAccessError err) {
            throw new IllegalStateException("Readability mismatch in inheritance hierarchy of class [" +
                    className + "]: " + err.getMessage(), err);
        } catch (Throwable ex) {
            // Typically ClassNotFoundException or NoClassDefFoundError...
            return false;
        }
    }

    /**
     * 获取ClassLoader
     *
     * @param clazz clazz
     * @return class loader
     */
    public static ClassLoader getClassLoader(Class<?> clazz) {
        ClassLoader cl = null;
        try {
            cl = Thread.currentThread().getContextClassLoader();
        } catch (Throwable ex) {
            // 无法访问线程上下文ClassLoader-退回到系统类加载器...
        }
        if (cl == null) {
            // 没有线程上下文类加载器->使用此类的类加载器。
            cl = clazz.getClassLoader();
            if (cl == null) {
                // getClassLoader（）返回null表示是使用引导ClassLoader
                try {
                    cl = ClassLoader.getSystemClassLoader();
                } catch (Throwable ex) {
                    // ignore
                }
            }
        }

        return cl;
    }

    /**
     * 返回要使用的默认ClassLoader：通常是线程上下文ClassLoader（如果有）；
     * 否则，返回默认值。即加载ClassUtils类的ClassLoader。
     * 如果打算在绝对需要非空ClassLoader引用的情况下使用线程上下文ClassLoader，
     * 请调用此方法：例如，用于类路径资源加载（但不一定用于Class.forName，该类接受null的ClassLoader引用为好）。
     *
     * @return class loader
     */
    public static ClassLoader getDefaultClassLoader() {
        return getClassLoader(ClassUtils.class);
    }
}
