package mayfly.common.validation;


import mayfly.common.utils.annotation.AnnotationUtils;
import mayfly.common.utils.ReflectionUtils;
import mayfly.common.validation.annotation.*;
import mayfly.common.validation.annotation.validator.*;

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
    private static Map<Class<? extends Annotation>, Validator[]> validatorCache = new ConcurrentHashMap<>(32);

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
    }

    private static ValidationHandler instance = new ValidationHandler();
    private ValidationHandler(){}

    /**
     * 单例入口
     * @return 参数检验处理器
     */
    public static ValidationHandler getInstance() {
        return instance;
    }

    /**
     * 校验对象中字段是否符合字段注解上的规则
     * @param obj  需要校验参数值的对象
     * @throws ParamValidErrorException  若不符合指定注解的参数值则抛出该异常
     */
    public void validate(Object obj) throws ParamValidErrorException {
        for (FieldInfo fieldValidators : getAllFieldInfo(obj)) {
            Field field = fieldValidators.field;
            Object fieldValue = ReflectionUtils.getFieldValue(field, obj);
            //遍历field字段需要校验的校验器
            for(Class<? extends Annotation> anno : fieldValidators.validAnnotations) {
                Validator[] validators = validatorCache.computeIfAbsent(anno, key -> {
                    ValidateBy vb = AnnotationUtils.getAnnotation(anno, ValidateBy.class);
                    if (vb == null) {
                        throw new IllegalArgumentException(String.format("@%s注解上没有对应@ValidateBy注解", anno.getSimpleName()));
                    }
                    return Stream.of(vb.value()).map(clazz -> {
                        try {
                            return clazz.newInstance();
                        } catch (Exception e) {
                            throw new IllegalArgumentException("实例化Validator失败", e);
                        }
                    }).toArray(Validator[]::new);
                });

                for (Validator validator : validators) {
                    @SuppressWarnings("unchecked")
                    ValidResult result = validator.validation(AnnotationUtils.getAnnotation(field, anno)
                            , Value.of(field.getName(), fieldValue));
                    if (!result.isRight()) {
                        throw new ParamValidErrorException(result.getMessage());
                    }
                }
            }
        }
    }


    /**
     * 获取指定对象中所有字段基本信息（含有哪些校验器）
     * @param obj
     * @return
     */
    public List<FieldInfo> getAllFieldInfo(Object obj) {
        return cache.computeIfAbsent(obj.getClass(), key -> {
            List<FieldInfo> allFieldValidators = new ArrayList<>(8);
            for (Field field : ReflectionUtils.getFields(key)) {
                Optional.ofNullable(getFieldInfo(field)).ifPresent(allFieldValidators::add);
            }
            return allFieldValidators;
        });
    }

    /**
     * 创建fieldInfo对象
     * @param field
     * @return 如果field字段不包含任何校验注解则返回null
     */
    private FieldInfo getFieldInfo(Field field) {
        // 该字段上所包含的校验注解
        List<Class<? extends Annotation>> validAnnotations = null;
        // 获取所有注册过的注解校验类
        for(Class<? extends Annotation> anno : validAnnotationRegister){
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

        public FieldInfo(){}

        public FieldInfo(Field field, List<Class<? extends Annotation>> validAnnotations) {
            this.field = field;
            this.validAnnotations = validAnnotations;
        }
    }
}
