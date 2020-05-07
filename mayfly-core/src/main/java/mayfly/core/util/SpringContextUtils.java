package mayfly.core.util;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

/**
 * @author meilin.huang
 * @date 2020-04-30 9:16 上午
 */
public class SpringContextUtils implements ApplicationContextAware {

    private static ApplicationContext context = null;

    private SpringContextUtils() {
        super();
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        context = applicationContext;
    }

    /**
     * 根据名称获取bean
     *
     * @param beanName bean name
     * @return bean
     */
    @SuppressWarnings("unchecked")
    public static <T> T getBean(String beanName) {
        return (T) context.getBean(beanName);
    }

    /**
     * 根据bean名称获取指定类型bean
     *
     * @param beanName bean名称
     * @param clazz    返回的bean类型,若类型不匹配,将抛出异常
     */
    public static <T> T getBean(String beanName, Class<T> clazz) {
        return context.getBean(beanName, clazz);
    }

    /**
     * 根据类型获取bean
     *
     * @param clazz class
     * @return bean
     */
    public static <T> T getBean(Class<T> clazz) {
        return context.getBean(clazz);
    }

    /**
     * 根据bean类型获取beanName数组
     *
     * @param type type
     * @return beanNames
     */
    public static String[] getBeanNamesForType(Class<?> type) {
        return context.getBeanNamesForType(type);
    }

    /**
     * 是否包含bean
     *
     * @param beanName bean name
     * @return bean
     */
    public static boolean containsBean(String beanName) {
        return context.containsBean(beanName);
    }

    /**
     * bean的类型
     *
     * @param beanName bean name
     * @return class
     */
    public static Class<?> getType(String beanName) {
        return context.getType(beanName);
    }
}
