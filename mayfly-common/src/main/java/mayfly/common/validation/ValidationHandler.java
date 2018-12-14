package mayfly.common.validation;


import mayfly.common.validation.annotation.*;
import mayfly.common.validation.annotation.validator.*;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author meilin.huang
 * @version 1.0
 * @description:
 * @date 2018-10-27 4:57 PM
 */
public class ValidationHandler {

    /**
     * 字段校验缓存，key:className   value: fieldInfo对象
     */
    private static final Map<String, List<FieldInfo>> CATCH = new ConcurrentHashMap<>(32);

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
    }

    private static ValidationHandler instance = new ValidationHandler();
    private ValidationHandler(){}

    /**
     * 单例入口
     * @return
     */
    public static ValidationHandler getInstance() {
        return instance;
    }

    /**
     * 校验对象中字段是否符合字段注解上的规则
     * @param obj  需要校验参数值的对象
     * @throws ParamErrorException  若不符合指定注解的参数值则抛出该异常
     */
    public void validate(Object obj) throws ParamErrorException {
        for (FieldInfo fieldInfo : getFieldInfoList(obj)) {
            Field field = fieldInfo.field;
            Object fieldValue = null;
            try {
                field.setAccessible(true);
                fieldValue = field.get(obj);
            } catch (Exception e) {
                throw new ParamErrorException(fieldInfo.field.getName() + "参数异常");
            }
            //遍历field字段需要校验的校验器
            for(Validator validator : fieldInfo.validators) {
                ValidResult result = validator.validation(field, fieldValue);
                if (!result.isRight()) {
                    throw new ParamErrorException(result.getMessage());
                }
            }
        }
    }

    /**
     * 获取指定对象中所有字段基本信息（含有哪些校验器）
     * @param obj
     * @return
     */
    public List<FieldInfo> getFieldInfoList(Object obj) {
        Class clazz = obj.getClass();
        String className = clazz.getName();

        List<FieldInfo> fieldInfoList = CATCH.get(className);
        if (fieldInfoList == null) {
            fieldInfoList = new ArrayList<>(8);
            for (Field field : clazz.getDeclaredFields()) {
                FieldInfo fi = buildFieldInfo(field);
                if (fi != null) {
                    fieldInfoList.add(fi);
                }
            }
            CATCH.put(className, fieldInfoList);
        }
        return fieldInfoList;
    }

    /**
     * 创建fieldInfo对象
     * @param field
     * @return 如果field字段不包含任何校验注解则返回null
     */
    private FieldInfo buildFieldInfo(Field field) {
        //该字段上需要校验的校验器类型列表
        List<Validator> validators = new ArrayList<>(8);
        //获取所有注册过的注解校验类
        for(Map.Entry<Class<? extends Annotation>, Validator> entry : validatorRegister.entrySet()){
            if (field.isAnnotationPresent(entry.getKey())) {
                validators.add(entry.getValue());
            }
        }
        //如果校验器为空，则说明该字段无需任何校验
        if (validators.isEmpty()) {
            return null;
        }

        return new FieldInfo(field, validators);
    }


    /**
     * 字段包含的所有注解校验器
     */
    private static class FieldInfo {
        private Field field;

        private List<Validator> validators;

        public FieldInfo(){}

        public FieldInfo(Field field, List<Validator> validators) {
            this.field = field;
            this.validators = validators;
        }
    }
}
