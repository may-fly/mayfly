package mayfly.common.validation;


import mayfly.common.utils.AnnotationUtils;
import mayfly.common.utils.ReflectionUtils;
import mayfly.common.validation.annotation.*;
import mayfly.common.validation.annotation.validator.*;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author meilin.huang
 * @version 1.0
 * @date 2018-10-27 4:57 PM
 */
public class ValidationHandler {

    /**
     * 字段校验缓存，key:class   value: fieldInfo对象列表
     */
    private static final Map<Class<?>, List<FieldValidators>> CACHE = new ConcurrentHashMap<>(32);

    /**
     * 校验器注册
     */
    private static Map<Class<? extends Annotation>, Validator> validatorRegister = new LinkedHashMap<>(8);
    static {
        validatorRegister.put(NotNull.class, new NotNullValidator());
        validatorRegister.put(NotBlank.class, new NotBlankValidator());
        validatorRegister.put(ValueIn.class, new ValueInValidator());
        validatorRegister.put(Size.class, new SizeValidator());
        validatorRegister.put(Pattern.class, new PatternValidator());
        validatorRegister.put(EnumValue.class, new EnumValueValidator());
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
        for (FieldValidators fieldValidators : getAllFieldValidators(obj)) {
            Field field = fieldValidators.field;
            Object fieldValue = ReflectionUtils.getFieldValue(field, obj);
            //遍历field字段需要校验的校验器
            for(Validator validator : fieldValidators.validators) {
                ValidResult result = validator.validation(field, fieldValue);
                if (!result.isRight()) {
                    throw new ParamValidErrorException(result.getMessage());
                }
            }
        }
    }


    /**
     * 获取指定对象中所有字段基本信息（含有哪些校验器）
     * @param obj
     * @return
     */
    public List<FieldValidators> getAllFieldValidators(Object obj) {
        return CACHE.computeIfAbsent(obj.getClass(), key -> {
            List<FieldValidators> allFieldValidators = new ArrayList<>(8);
            for (Field field : ReflectionUtils.getFields(key)) {
                Optional.ofNullable(getFieldValidators(field)).ifPresent(allFieldValidators::add);
            }
            return allFieldValidators;
        });
    }

    /**
     * 创建fieldInfo对象
     * @param field
     * @return 如果field字段不包含任何校验注解则返回null
     */
    private FieldValidators getFieldValidators(Field field) {
        //该字段上需要校验的校验器类型列表
        List<Validator> validators = null;
        //获取所有注册过的注解校验类
        for(Map.Entry<Class<? extends Annotation>, Validator> entry : validatorRegister.entrySet()){
            if (AnnotationUtils.isAnnotationPresent(field, entry.getKey())) {
                if (validators == null) {
                    validators = new ArrayList<>(4);
                }
                validators.add(entry.getValue());
            }
        }
        //如果校验器为空，则说明该字段无需任何校验
        return validators == null ? null : new FieldValidators(field, validators);
    }


    /**
     * 字段包含的所有注解校验器
     */
    private static class FieldValidators {
        private Field field;

        private List<Validator> validators;

        public FieldValidators(){}

        public FieldValidators(Field field, List<Validator> validators) {
            this.field = field;
            this.validators = validators;
        }
    }
}
