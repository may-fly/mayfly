package mayfly.core.util;

import mayfly.core.util.annotation.AnnotationUtils;
import mayfly.core.util.enums.EnumUtils;
import mayfly.core.util.enums.NameValueEnum;

import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.WeakHashMap;
import java.util.stream.Collectors;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * @author hml
 * @version 1.0
 * @date 2018-11-17 2:13 PM
 */
public class BeanUtils {

    /**
     * 转换器缓存
     */
    private static Map<Class<? extends FieldValueConverter>, FieldValueConverter> converterCache = Collections.synchronizedMap(new WeakHashMap<>(8));

    /**
     * 实例化对象
     *
     * @param clazz 对象类型
     * @param <T>   对象泛型类
     * @return 对象
     */
    public static <T> T instantiate(Class<T> clazz) {
        Assert.assertState(!clazz.isInterface(), "无法实例化接口：" + clazz.getName());
        try {
            return clazz.getDeclaredConstructor().newInstance();
        } catch (Exception e) {
            throw new IllegalStateException("实例化对象失败", e);
        }
    }

    /**
     * 实例化对象
     *
     * @param constructor 构造器
     * @param <T>         对象泛型类
     * @return 对象
     */
    public static <T> T instantiate(Constructor<T> constructor, Object... initargs) {
        try {
            return constructor.newInstance(initargs);
        } catch (Exception e) {
            throw new IllegalStateException("实例化对象失败", e);
        }
    }

    /**
     * 获取bean的属性描述器
     *
     * @param clazz bean类型
     * @return
     */
    public static PropertyDescriptor[] getPropertyDescriptors(Class<?> clazz) {
        try {
            return Introspector.getBeanInfo(clazz).getPropertyDescriptors();
        } catch (IntrospectionException e) {
            throw new IllegalArgumentException("获取BeanInfo异常!", e);
        }
    }


    /**
     * 将的bean转为map, key:fieldName, value:fieldValue  <br/> <br/>
     * 如果bean的属性中还有bean，则key为以前一个beanName.fieldName
     *
     * @param bean
     * @return
     */
    public static Map<String, Object> bean2Map(Object bean) {
        return doBean2Map(null, bean);
    }

    /**
     * 将list中的bean转为map, key:fieldName, value:fieldValue  <br/> </br/>
     * 如果bean的属性中还有bean，则key为以前一个beanName.fieldName
     *
     * @param beans
     * @return
     */
    public static List<Map<String, Object>> beans2Maps(Collection<?> beans) {
        return beans.stream().map(BeanUtils::bean2Map).collect(Collectors.toList());
    }

    /**
     * 将map对象里的key(属性名)-value(属性值)转换为bean属性值
     *
     * @param sourceMap map
     * @param clazz     bean类型
     * @param <T>       bean的具体类型
     * @return 实例bean
     */
    public static <T> T map2Bean(Map<String, Object> sourceMap, Class<T> clazz) {
        T target = BeanUtils.instantiate(clazz);
        for (PropertyDescriptor pd : getPropertyDescriptors(clazz)) {
            Object fieldValue = sourceMap.get(pd.getName());
            if (fieldValue != null) {
                Method writeMethod = pd.getWriteMethod();
                if (fieldValue.getClass() != writeMethod.getParameterTypes()[0]) {
                    throw new IllegalStateException("参数类型不匹配！");
                }
                ReflectionUtils.invokeMethod(writeMethod, target, fieldValue);
            }
        }
        return target;
    }

    /**
     * 将bean转为map,递归转换bean中非常用基本类型
     *
     * @param prefix map中key字段的前缀，即map中key为perfix + "." + fieldName
     * @param bean
     * @return
     */
    private static Map<String, Object> doBean2Map(String prefix, Object bean) {
        if (bean == null) {
            return null;
        }
        Class<?> type = bean.getClass();
        Map<String, Object> returnMap = new HashMap<>(32);
        // 遍历属性描述器
        for (PropertyDescriptor descriptor : getPropertyDescriptors(type)) {
            String propertyName = descriptor.getName();
            if ("class".equals(propertyName)) {
                continue;
            }
            Object result = ReflectionUtils.invokeMethod(descriptor.getReadMethod(), bean);
            if (result == null) {
                continue;
            }
            // 如果非基本类型
            if (!isSimpleValue(result)) {
                if (ObjectUtils.isCollection(result)) {
                    returnMap.put(parsePropertyName(prefix, propertyName), beans2Maps((Collection) result));
                    continue;
                }
                if (ObjectUtils.isMap(result)) {
                    returnMap.put(parsePropertyName(prefix, propertyName), result);
                    continue;
                }
                // 递归转换属性Bean
                returnMap.putAll(doBean2Map(propertyName, result));
                continue;
            }
            Bean2MapFieldConverter converterAnnotation = AnnotationUtils
                    .getAnnotation(ReflectionUtils.getField(type, propertyName), Bean2MapFieldConverter.class);
            if (converterAnnotation != null) {
                // 转换值
                result = convertValue(converterAnnotation, result);
                // 判断是否需要重命名key
                String rename = converterAnnotation.rename();
                if (!"".equals(rename.trim())) {
                    propertyName = rename;
                }
            }
            returnMap.put(parsePropertyName(prefix, propertyName), result);
        }
        return returnMap;
    }

    private static String parsePropertyName(String prefix, String propertyName) {
        return prefix != null ? prefix + "." + propertyName : propertyName;
    }

    @SuppressWarnings("unchecked")
    private static Object convertValue(Bean2MapFieldConverter converter, Object value) {
        Class<? extends FieldValueConverter> converterClazz = converter.converter();
        // 如果FieldValueConverter不是默认的转换器，就使用该转换器
        if (converterClazz != FieldValueConverter.class) {
            // 转换器缓存中获取
            FieldValueConverter fc = converterCache.get(converterClazz);
            if (fc == null) {
                fc = BeanUtils.instantiate(converterClazz);
                converterCache.put(converterClazz, fc);
            }
            // 转换值
            return fc.convert(value);
        }

        Class<? extends Enum> enumClass = converter.enumConverter();
        if (enumClass != Bean2MapFieldConverter.DefaultEnum.class && value instanceof Integer) {
            return EnumUtils.getNameByValue(ObjectUtils.cast(enumClass.getEnumConstants(), NameValueEnum.class), (Integer) value);
        }

        return value;
    }

    /**
     * 判断对象是否为简单基本类型
     *
     * @param res
     * @return
     */
    public static boolean isSimpleValue(Object res) {
        return ObjectUtils.isWrapperOrPrimitive(res) || ObjectUtils.isEnum(res) || ObjectUtils.isDate(res);
    }

    /**
     * Bean字段值转换 </br>
     * 泛型 T:bean中原始值类型 V:转换后的值类型，
     *
     * @author meilin.huang
     * @version 1.0
     * @date 2019-03-06 6:42 PM
     */
    public interface FieldValueConverter<T, V> {

        /**
         * 字段值转换, 如将枚举值Integer转换为String类型的name <br/>
         * V:转换后的值， T:bean中原始值
         *
         * @param fieldValue 真实字段值
         */
        V convert(T fieldValue);
    }

    /**
     * bean属性转换器注解  </br>
     *
     * @author meilin.huang
     * @version 1.0
     * @date 2019-03-06 6:42 PM
     */
    @Target({FIELD})
    @Retention(RUNTIME)
    public @interface Bean2MapFieldConverter {
        /**
         * 重命名
         */
        String rename() default "";

        /**
         * 值转换器
         */
        Class<? extends BeanUtils.FieldValueConverter> converter() default FieldValueConverter.class;

        /**
         * 枚举值转换,枚举类必须继承EnumValue接口
         */
        Class<? extends Enum<? extends NameValueEnum>> enumConverter() default DefaultEnum.class;

        enum DefaultEnum implements NameValueEnum<Integer> {
            ;

            @Override
            public Integer getValue() {
                return 0;
            }

            @Override
            public String getName() {
                return null;
            }
        }
    }
}
