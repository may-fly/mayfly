package mayfly.common.utils;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.*;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * @author hml
 * @version 1.0
 * @description:
 * @date 2018-11-17 2:13 PM
 */
public class BeanUtils {

    /**
     * 转换器缓存
     */
    private static Map<Class<? extends FieldValueConverter>, FieldValueConverter> converterCache = Collections.synchronizedMap(new WeakHashMap<Class<? extends FieldValueConverter>, FieldValueConverter>(32));

    /**
     * 获取指定类的指定field,包括父类
     * @param clazz
     * @param name
     * @return
     */
    public static Field getField(Class clazz, String name) {
        while (clazz != null) {
            Field field = null;
            try {
                field = clazz.getDeclaredField(name);
            } catch (NoSuchFieldException e) {
                clazz = clazz.getSuperclass();
            }
            if (field != null) {
                return field;
            }
        }
        return null;
    }

    /**
     * 获取指定类的所有非static的field,包括父类
     * @param clazz
     * @return
     */
    public static Field[] getFields(Class clazz) {
        List<Field> fields = new ArrayList<>(32);
        while (clazz != null) {
            for (Field field : clazz.getDeclaredFields()) {
                if (!Modifier.isStatic(field.getModifiers())) {
                    fields.add(field);
                }
            }
            clazz = clazz.getSuperclass();
        }
        return fields.toArray(new Field[fields.size()]);
    }

    /**
     * 将list中的bean转为map, key:fieldName, value:fieldValue  <br/> </br/>
     * 如果bean的属性中还有bean，则key为以前一个beanName.fieldName
     * @param beans
     * @return
     */
    public static List<Map<String, Object>> beans2Maps(Collection beans) {
        List result = new ArrayList(beans.size());
        for (Object obj : beans) {
            result.add(bean2Map(obj));
        }
        return result;
    }

    /**
     * 将的bean转为map, key:fieldName, value:fieldValue  <br/> <br/>
     * 如果bean的属性中还有bean，则key为以前一个beanName.fieldName
     * @param bean
     * @return
     */
    public static Map<String, Object> bean2Map(Object bean) {
        return doBean2Map(null, bean);
    }

    /**
     * 将bean转为map,递归转换bean中非常用基本类型
     * @param prefix map中key字段的前缀，即map中key为perfix + "." + fieldName
     * @param bean
     * @return
     */
    private static Map<String, Object> doBean2Map(String prefix, Object bean) {
        if (bean == null) {
            return null;
        }
        Class type = bean.getClass();
        Map<String ,Object> returnMap = new HashMap(32);
        // 获取对象beanInfo
        BeanInfo beanInfo;
        try {
            beanInfo = Introspector.getBeanInfo(type);
        } catch (IntrospectionException e) {
            throw new IllegalArgumentException("无法获取其BeanInfo", e);
        }
        for (PropertyDescriptor descriptor : beanInfo.getPropertyDescriptors()) {
            String propertyName = descriptor.getName();
            if (!"class".equals(propertyName)) {
                Method readMethod = descriptor.getReadMethod();
                Object result;
                try {
                    result = readMethod.invoke(bean, new Object[0]);
                } catch (IllegalAccessException | InvocationTargetException e) {
                    throw new IllegalArgumentException("无法获取Bean属性值", e);
                }
                if (result == null) {
                    continue;
                }
                // 如果非基本类型
                if (!isSimpleValue(result)) {
                    if (isCollection(result)) {
                        returnMap.put(parsePropertyName(prefix, propertyName), beans2Maps((Collection)result));
                        continue;
                    }
                    if (isMap(result)) {
                        returnMap.put(parsePropertyName(prefix, propertyName), result);
                        continue;
                    }
                    // 递归转换属性Bean
                    returnMap.putAll(doBean2Map(propertyName, result));
                    continue;
                }
                Bean2MapFieldConverter converterAnnotation = getField(type, propertyName).getAnnotation(Bean2MapFieldConverter.class);
                if (converterAnnotation != null) {
                    Class<? extends FieldValueConverter> converterClazz = converterAnnotation.converter();
                    // 转换器缓存中获取
                    FieldValueConverter fc = converterCache.get(converterClazz);
                    if (fc == null) {
                        try {
                            fc = converterClazz.newInstance();
                        } catch (InstantiationException | IllegalAccessException e) {
                            throw new IllegalArgumentException("实例化field value转换器失败", e);
                        }
                        converterCache.put(converterClazz, fc);
                    }
                    // 转换值
                    result = fc.convert(result);
                    // 判断是否需要重命名key
                    String rename = converterAnnotation.rename();
                    if (!"".equals(rename.trim())) {
                        propertyName = rename;
                    }
                }
                returnMap.put(parsePropertyName(prefix, propertyName), result);
            }
        }
        return returnMap;
    }

    private static String parsePropertyName(String prefix, String propertyName) {
        return prefix != null ? prefix + "." + propertyName : propertyName;
    }

    /**
     * 判断对象是否为简单基本类型
     * @param res
     * @return
     */
    public static boolean isSimpleValue(Object res) {
        Class clazz = res.getClass();
        return Enum.class.isAssignableFrom(clazz)
                || CharSequence.class.isAssignableFrom(clazz)
                || Number.class.isAssignableFrom(clazz)
                || Date.class.isAssignableFrom(clazz);
    }

    private static boolean isCollection(Object res) {
        return Collection.class.isAssignableFrom(res.getClass());
    }

    private static boolean isMap(Object res) {
        return Map.class.isAssignableFrom(res.getClass());
    }



    /**
     * Bean字段值转换 </br>
     * 泛型 T:bean中原始值类型 V:转换后的值类型，
     * @author meilin.huang
     * @version 1.0
     * @date 2019-03-06 6:42 PM
     */
    public interface FieldValueConverter<T, V> {

        /**
         * 字段值转换, 如将枚举值Integer转换为String类型的name <br/>
         * V:转换后的值， T:bean中原始值
         * @param fieldValue  真实字段值
         * @return
         */
        V convert(T fieldValue);
    }

    /**
     * bean属性转换器注解  </br>
     * @author meilin.huang
     * @version 1.0
     * @date 2019-03-06 6:42 PM
     */
    @Target({ FIELD })
    @Retention(RUNTIME)
    public @interface Bean2MapFieldConverter {
        /**
         * 重命名
         * @return
         */
        String rename() default "";

        /**
         * 值转换器
         * @return
         */
        Class<? extends BeanUtils.FieldValueConverter> converter();
    }
}
