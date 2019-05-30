package mayfly.common.validation.aop;

import mayfly.common.utils.AnnotationUtils;
import mayfly.common.validation.ParamValidErrorException;
import mayfly.common.validation.annotation.Valid;
import mayfly.common.validation.ValidationHandler;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author meilin.huang
 * @version 1.0
 * @date 2018-11-22 11:27 AM
 */
public class AopParamValidator {

    /**
     * 带有@Valid参数的索引位置, key:方法全路径  value:索引列表（即参数可以含有多个@Valid注解）
     */
    private static Map<Method, List<Integer>> indexCache = new ConcurrentHashMap<>(64);


    private AopParamValidator(){}

    private static AopParamValidator validator = new AopParamValidator();

    public static AopParamValidator getInstance() {
        return validator;
    }


    public void validate(Method method, Object[] args) throws ParamValidErrorException {
        List<Integer> idx = indexCache.computeIfAbsent(method, key -> {
            List<Integer> index = null;
            Parameter[] params = method.getParameters();
            for (int i = 0; i < params.length; i++) {
                if(AnnotationUtils.isAnnotationPresent(params[i], Valid.class)) {
                    if (index == null) {
                        index = new ArrayList<>();
                    }
                    index.add(i);
                }
            }
            if (index == null) {
                index = Collections.emptyList();
            }
            return index;
        });
        //没有需要校验的参数索引，直接返回
        if (idx.isEmpty()) {
            return;
        }
        for (Integer i : idx) {
            ValidationHandler.getInstance().validate(args[i]);
        }
    }

}
