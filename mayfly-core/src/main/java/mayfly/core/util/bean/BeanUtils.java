package mayfly.core.util.bean;

import com.github.dozermapper.core.DozerBeanMapperBuilder;
import com.github.dozermapper.core.Mapper;
import mayfly.core.util.Assert;
import mayfly.core.util.CollectionUtils;
import mayfly.core.util.ObjectUtils;
import mayfly.core.util.ReflectionUtils;
import mayfly.core.util.annotation.AnnotationUtils;
import mayfly.core.util.enums.EnumUtils;
import mayfly.core.util.enums.NameValueEnum;

import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.WeakHashMap;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * @author hml
 * @version 1.0
 * @date 2018-11-17 2:13 PM
 */
public class BeanUtils {

    /**
     * 转换器缓存
     */
    @SuppressWarnings("all")
    private static Map<Class<? extends FieldValueConverter>, FieldValueConverter> converterCache = Collections.synchronizedMap(new WeakHashMap<>(8));

    private static final Mapper DEFAULT_MAPPER = DozerBeanMapperBuilder.buildDefault();

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
            return instantiate(clazz.getDeclaredConstructor());
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
            throw new IllegalStateException(String.format("实例化%s对象失败", constructor.getDeclaringClass().getName()), e);
        }
    }

    public static void copy(Object source, Object target) {
        if (source == null) {
            return;
        }
        DEFAULT_MAPPER.map(source, target);
    }

    public static <T> T copy(Object source, Class<T> targetClass) {
        if (source == null) {
            return null;
        }
        return DEFAULT_MAPPER.map(source, targetClass);
    }

    public static <T> List<T> copy(List<?> source, Class<T> targetClass) {
        if (CollectionUtils.isEmpty(source)) {
            return Collections.emptyList();
        }
        return source.stream().map(s -> copy(s, targetClass)).collect(Collectors.toList());
    }

    /**
     * 获取bean的属性描述器
     *
     * @param clazz bean类型
     * @return PropertyDescriptor
     */
    public static PropertyDescriptor[] getPropertyDescriptors(Class<?> clazz) {
        try {
            return Introspector.getBeanInfo(clazz).getPropertyDescriptors();
        } catch (IntrospectionException e) {
            throw new IllegalArgumentException("获取BeanInfo异常!", e);
        }
    }

    /**
     * 获取两个对象之间属性值改变记录列表（对象类型可不同，只要字段名和类型相同即可）
     *
     * @param newObj 新对象
     * @param old    旧对象
     * @return 对象值改变列表
     */
    public static List<FieldValueChangeRecord> getFieldValueChangeRecords(Object newObj, Object old) {
        return getFieldValueChangeRecords(newObj, old, null);
    }

    /**
     * 获取两个对象之间属性值改变记录列表（对象类型可不同，只要字段名和类型相同即可）
     *
     * @param newObj    新对象
     * @param old       旧对象
     * @param predicate 字段过滤器
     * @return 对象值改变列表
     */
    public static List<FieldValueChangeRecord> getFieldValueChangeRecords(Object newObj, Object old, Predicate<Field> predicate) {
        Class<?> oldObjClass = old.getClass();
        List<FieldValueChangeRecord> changeRecords = new ArrayList<>();
        for (Field nf : ReflectionUtils.getFields(newObj.getClass())) {
            if (predicate != null && !predicate.test(nf)) {
                continue;
            }
            String fieldName = nf.getName();
            // 旧值不存在指定字段，直接跳过
            Field oldObjFiled = ReflectionUtils.getField(oldObjClass, fieldName, nf.getType());
            if (oldObjFiled == null || Modifier.isStatic(oldObjFiled.getModifiers())) {
                continue;
            }
            Object newValue = ReflectionUtils.getFieldValue(nf, newObj);
            Object oldValue = ReflectionUtils.getFieldValue(oldObjFiled, old);
            if (!Objects.equals(oldValue, newValue)) {
                // 判断字段值是否需要转换，如将枚举值'1'转换为有实际意义的字符串
                FieldValueConvert convert = AnnotationUtils.getAnnotation(nf, FieldValueConvert.class);
                convert = convert == null ? AnnotationUtils.getAnnotation(oldObjFiled, FieldValueConvert.class) : convert;
                if (convert != null) {
                    newValue = convertValue(convert, newValue);
                    oldValue = convertValue(convert, oldValue);
                }
                changeRecords.add(new FieldValueChangeRecord(fieldName, oldValue, newValue));
            }
        }
        return changeRecords;
    }


    /**
     * 将的bean转为map, key:fieldName, value:fieldValue  <br/> <br/>
     * 如果bean的属性中还有bean，则key为以前一个beanName.fieldName
     *
     * @param bean bean
     * @return map
     */
    public static Map<String, Object> bean2Map(Object bean) {
        return doBean2Map(null, bean);
    }

    /**
     * 将list中的bean转为map, key:fieldName, value:fieldValue  <br/> </br/>
     * 如果bean的属性中还有bean，则key为以前一个beanName.fieldName
     *
     * @param beans beans
     * @return maps
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
     * @param bean   bean
     * @return map
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
            FieldValueConvert converterAnnotation = AnnotationUtils
                    .getAnnotation(ReflectionUtils.getField(type, propertyName), FieldValueConvert.class);
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

    @SuppressWarnings("all")
    private static Object convertValue(FieldValueConvert convertAnno, Object value) {
        Class<? extends FieldValueConverter> converterClazz = convertAnno.converter();
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

        Class<? extends Enum> enumClass = convertAnno.enumConverter();
        if (enumClass != FieldValueConvert.DefaultEnum.class) {
            return EnumUtils.getNameByValue((NameValueEnum[]) enumClass.getEnumConstants(), value);
        }

        return value;
    }

    /**
     * 判断对象是否为简单基本类型
     *
     * @param res object
     * @return true or false
     */
    public static boolean isSimpleValue(Object res) {
        return ObjectUtils.isWrapperOrPrimitive(res) || ObjectUtils.isEnum(res) || ObjectUtils.isDate(res);
    }
}
