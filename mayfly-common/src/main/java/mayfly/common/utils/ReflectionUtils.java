package mayfly.common.utils;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author meilin.huang
 * @version 1.0
 * @date 2019-03-27 10:04 AM
 */
public final class ReflectionUtils {


    /**
     * 获取所有field字段，包含父类继承的
     *
     * @param clazz  字段所属类型
     * @return
     */
    public static Field[] getFields(Class<?> clazz) {
        return getFields(clazz, null);
    }

    /**
     * 获取指定类的所有的field,包括父类
     *
     * @param clazz  字段所属类型
     * @param fieldFilter 字段过滤器
     * @return  符合过滤器条件的字段数组
     */
    public static Field[] getFields(Class<?> clazz, FieldFilter fieldFilter) {
        List<Field> fields = new ArrayList<>(32);
        while (Object.class != clazz && clazz != null) {
            for (Field field : clazz.getDeclaredFields()) {
                if (fieldFilter != null && !fieldFilter.matches(field)) {
                    continue;
                }
                fields.add(field);
            }
            clazz = clazz.getSuperclass();
        }
        return fields.toArray(new Field[fields.size()]);
    }

    /**
     * 获取指定类的指定field,包括父类
     *
     * @param clazz  字段所属类型
     * @param name  字段名
     * @return
     */
    public static Field getField(Class<?> clazz, String name) {
        return getField(clazz, name, null);
    }

    /**
     * 获取指定类的指定field,包括父类
     *
     * @param clazz  字段所属类型
     * @param name  字段名
     * @param type  field类型
     * @return
     */
    public static Field getField(Class<?> clazz, String name, Class<?> type) {
        Assert.notNull(clazz, "clazz不能为空！");
        while (clazz != Object.class && clazz != null) {
            for (Field field : clazz.getDeclaredFields()) {
                if ((name == null || name.equals(field.getName())) &&
                        (type == null || type.equals(field.getType()))) {
                    return field;
                }
            }
            clazz = clazz.getSuperclass();
        }
        throw new IllegalStateException(clazz.getName() + "." + name + "字段不存在！");
    }

    /**
     * 获取字段值
     * @param field  字段
     * @param target  字段所属实例对象
     * @return
     */
    public static Object getFieldValue(Field field, Object target) {
        makeAccessible(field);
        try {
            return field.get(target);
        } catch (Exception e) {
            throw new IllegalStateException("获取field值错误！");
        }
    }

    /**
     * 获取对象中指定field值
     * @param obj  对象
     * @param fieldName  字段名
     * @return
     */
    public static Object getFieldValue(Object obj, String fieldName) {
        Class<?> type = obj.getClass();
        if (isSimpleValueType(type)) {
            return obj;
        }
        return getFieldValue(getField(type, fieldName), obj);
    }

    /**
     * 获取指定对象中指定字段路径的值(类似js访问对象属性) <br/>
     *  如：Product p = new Product(new User())  <br/>
     *  可使用ReflectionUtils.getValueByFieldPath(p, "user.name")获取到用户的name属性
     *
     * @param obj   取值对象
     * @param fieldPath  字段路径(形如 user.name)
     * @return  字段value
     */
    public static Object getValueByFieldPath(Object obj, String fieldPath) {
        String[] fieldNames = fieldPath.split("\\.");
        Object result = null;
        for (String fieldName : fieldNames) {
            result = getFieldValue(obj, fieldName);
            if (result == null) {
                return null;
            }
            obj = result;
        }
        return result;
    }

    /**
     * 设置字段值
     * @param field  字段
     * @param target  字段所属对象实例
     * @param value  需要设置的值
     */
    public static void setFieldValue(Field field, Object target, Object value) {
        makeAccessible(field);
        try {
            field.set(target, value);
        } catch (Exception e) {
            throw new IllegalStateException("设置field值错误！");
        }
    }

    /**
     * 判断class是否为简单值类型
     * @param clazz
     * @return
     */
    public static boolean isSimpleValueType(Class<?> clazz) {
        return Enum.class.isAssignableFrom(clazz) || CharSequence.class.isAssignableFrom(clazz) || Number.class.isAssignableFrom(clazz) || Date.class.isAssignableFrom(clazz);
    }

    /**
     * 判断指定对象是否为简单值类型
     * @param obj
     * @return
     */
    public static boolean isSimpleValue(Object obj) {
        return isSimpleValueType(obj.getClass());
    }

    /**
     * 对指定类中的field进行操作，并返回操作结果
     *
     * @param clazz
     * @param name     field name
     * @param callback field回调
     * @param <T>
     * @return
     */
    public static <T> T doWithField(Class<?> clazz, String name, FieldCallback<T> callback) {
        Assert.notNull(clazz, "clazz不能为空！");
        Assert.notEmpty(name, "fieldName不能为空！");
        return doWithField(getField(clazz, name), callback);
    }

    /**
     * 对指定字段进行回调操作
     *
     * @param field
     * @param callback
     * @param <T>
     * @return
     */
    public static <T> T doWithField(Field field, FieldCallback<T> callback) {
        Assert.notNull(field, "field不能为空！");
        return callback.doWith(field);
    }

    /**
     * 设置字段为可见
     * @param field
     */
    public static void makeAccessible(Field field) {
        if ((!Modifier.isPublic(field.getModifiers()) ||
                !Modifier.isPublic(field.getDeclaringClass().getModifiers()) ||
                Modifier.isFinal(field.getModifiers())) && !field.isAccessible()) {
            field.setAccessible(true);
        }
    }




    /**
     * 字段过滤
     */
    @FunctionalInterface
    public interface FieldFilter {
        /**
         * 对指定字段进行过滤
         *
         * @param field
         * @return
         */
        boolean matches(Field field);
    }

    /**
     * 对指定字段进行回调
     *
     * @param <T>
     */
    public interface FieldCallback<T> {
        /**
         * 对字段进行操作，并返回操作结果
         *
         * @param field
         * @return
         */
        T doWith(Field field);
    }
}
