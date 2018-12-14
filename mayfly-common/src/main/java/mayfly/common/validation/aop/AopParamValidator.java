package mayfly.common.validation.aop;

import mayfly.common.validation.ParamErrorException;
import mayfly.common.validation.ValidationHandler;
import mayfly.common.validation.annotation.Valid;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
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
    private static Map<String, List<Integer>> indexCache = new ConcurrentHashMap<>(64);


    /**
     * 不包含@Valid的方法对应的value都指向该对象,节省不必要的内存
     */
    private static final List<Integer> EMPTY = new ArrayList<>();

    private AopParamValidator(){}

    private static AopParamValidator validator = new AopParamValidator();

    public static AopParamValidator getInstance() {
        return validator;
    }


    public void validate(Method method, Object[] args) throws ParamErrorException {
        String invoke = method.getDeclaringClass().getName() + "." + method.getName();
        List<Integer> index = indexCache.get(invoke);

        if (index == null) {
            Parameter[] params = method.getParameters();
            for (int i = 0; i < params.length; i++) {
                if(params[i].isAnnotationPresent(Valid.class)) {
                    if (index == null) {
                        index = new ArrayList<>();
                    }
                    index.add(i);
                }
            }
            if (index == null) {
                index = EMPTY;
            }
            indexCache.put(invoke, index);
        }

        for (Integer i : index) {
            ValidationHandler.getInstance().validate(args[i]);
        }
    }

}
