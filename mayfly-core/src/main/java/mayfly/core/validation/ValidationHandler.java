package mayfly.core.validation;


import mayfly.core.util.*;
import mayfly.core.util.annotation.AnnotationUtils;
import mayfly.core.util.bean.BeanUtils;
import mayfly.core.validation.annotation.*;
import mayfly.core.validation.annotation.validator.*;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Stream;

/**
 * @author meilin.huang
 * @version 1.0
 * @date 2018-10-27 4:57 PM
 */
public class ValidationHandler {

    /**
     * 字段校验缓存，key:class   value: FieldInfo对象列表
     */
    private static Map<Class<?>, List<FieldInfo>> cache = new ConcurrentHashMap<>(32);

    /**
     * 校验注解对应的校验器
     */
    private static Map<Class<? extends Annotation>, Validator<?, ?>[]> validatorCache = new ConcurrentHashMap<>(32);

    /**
     * 校验注解注册
     */
    private static Set<Class<? extends Annotation>> validAnnotationRegister = new HashSet<>(8);

    static {
        validAnnotationRegister.add(NotNull.class);
        validAnnotationRegister.add(NotBlank.class);
        validAnnotationRegister.add(Size.class);
        validAnnotationRegister.add(Pattern.class);
        validAnnotationRegister.add(EnumValue.class);
        // 支持字段递归校验
        validAnnotationRegister.add(Valid.class);
    }

    private static ValidationHandler instance = new ValidationHandler();

    private ValidationHandler() {
    }

    /**
     * 单例入口
     *
     * @return 参数检验处理器
     */
    public static ValidationHandler getInstance() {
        return instance;
    }

    /**
     * 校验对象中字段是否符合字段注解上的规则
     *
     * @param obj 需要校验参数值的对象
     * @throws ParamValidErrorException 若不符合指定注解的参数值则抛出该异常
     */
    @SuppressWarnings("all")
    public void validate(Object obj) throws ParamValidErrorException {
        Assert.notNull(obj, "校验对象不能为空！");
        // 如果是包装类型或者是java原生基本类型，则直接返回
        if (ObjectUtils.isWrapperOrPrimitive(obj)) {
            return;
        }
        for (FieldInfo fieldValidators : getAllFieldInfo(obj)) {
            Field field = fieldValidators.field;
            Object fieldValue = ReflectionUtils.getFieldValue(field, obj);
            // 遍历field字段需要校验的校验器
            for (Class<? extends Annotation> anno : fieldValidators.validAnnotations) {
                // 如果是Valid注解，则跳过
                if (anno == Valid.class) {
                    continue;
                }
                Validator[] validators = validatorCache.computeIfAbsent(anno, key -> {
                    ValidateBy vb = AnnotationUtils.getAnnotation(anno, ValidateBy.class);
                    Assert.notNull(vb, String.format("@%s注解上没有对应@ValidateBy注解", anno.getSimpleName()));
                    return Stream.of(vb.value()).map(BeanUtils::instantiate).toArray(Validator[]::new);
                });

                for (Validator validator : validators) {
                    Annotation validAnno = AnnotationUtils.getAnnotation(field, anno);
                    if (!validator.validation(validAnno, fieldValue)) {
                        throw new ParamValidErrorException(getErrorMessage(field, validAnno));
                    }
                }
            }
            // 如果字段类型含有@Valid注解，则递归进行校验
            if (AnnotationUtils.isAnnotationPresent(field, Valid.class)) {
                // 如果是集合列表，则遍历列表每个对象，并校验
                if (ObjectUtils.isCollection(fieldValue)) {
                    for (Object o : (Collection) fieldValue) {
                        validate(o);
                    }
                    continue;
                }
                // 如果是普通对象数组，也遍历每个数组元素
                if (ObjectUtils.isArray(fieldValue) && !ObjectUtils.isPrimitiveArray(fieldValue)) {
                    for (Object o : (Object[]) fieldValue) {
                        validate(o);
                    }
                    continue;
                }
                // 是普通对象
                if (fieldValue != null) {
                    validate(fieldValue);
                }
            }
        }
    }

    /**
     * 获取指定对象中所有字段基本信息（含有哪些校验器）
     *
     * @param obj 校验对象
     * @return fieldInfo {@link FieldInfo} list
     */
    public List<FieldInfo> getAllFieldInfo(Object obj) {
        return cache.computeIfAbsent(obj.getClass(), key -> {
            List<FieldInfo> allFieldValidators = new ArrayList<>(8);
            // 将有包含需要校验注解的字段加入列表
            ReflectionUtils.doWithFields(key, field -> Optional.ofNullable(getFieldInfo(field)).ifPresent(allFieldValidators::add));
            return allFieldValidators;
        });
    }

    /**
     * 获取不满足验证规则的错误消息
     *
     * @param annotation 校验注解
     * @return 错误信息
     */
    private String getErrorMessage(Field field, Annotation annotation) {
        // 获取注解的message属性值
        Object annoMessage = AnnotationUtils.getAttributeValue(annotation, "message");
        Assert.notNull(annoMessage, annotation.annotationType().getName() + "注解类必须含有message属性方法");
        // 替换错误消息提示
        String message = Objects.toString(annoMessage);
        Map<String, Object> attributeMap = AnnotationUtils.getAttributeMap(annotation);
        attributeMap.put("fieldName", field.getName());
        return BracePlaceholder.resolveByMap(message, attributeMap);
    }

    /**
     * 创建fieldInfo对象
     *
     * @param field 字段
     * @return 如果field字段不包含任何校验注解则返回null
     */
    private FieldInfo getFieldInfo(Field field) {
        // 该字段上所包含的校验注解
        List<Class<? extends Annotation>> validAnnotations = null;
        // 获取所有注册过的注解校验类
        for (Class<? extends Annotation> anno : validAnnotationRegister) {
            if (AnnotationUtils.isAnnotationPresent(field, anno)) {
                if (validAnnotations == null) {
                    validAnnotations = new ArrayList<>(4);
                }
                validAnnotations.add(anno);
            }
        }
        //如果校验注解为空，则说明该字段无需任何校验
        return validAnnotations == null ? null : new FieldInfo(field, validAnnotations);
    }


    /**
     * 字段包含的所有注解校验器
     */
    private static class FieldInfo {
        private Field field;

        /**
         * 字段包含的校验注解列表
         */
        private List<Class<? extends Annotation>> validAnnotations;

        public FieldInfo(Field field, List<Class<? extends Annotation>> validAnnotations) {
            this.field = field;
            this.validAnnotations = validAnnotations;
        }
    }
}
