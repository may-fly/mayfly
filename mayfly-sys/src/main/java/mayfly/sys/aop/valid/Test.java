package mayfly.sys.aop.valid;

import mayfly.common.validation.ParamValidErrorException;
import mayfly.common.validation.ValidationHandler;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * @author meilin.huang
 * @version 1.0
 * @date 2019-04-17 13:46
 */
public class Test {

    static class User {
        @NotBlank
        String name;
        @NotNull
        Integer sex;
        @NotBlank
        String password;
    }

    static class User2{
        @mayfly.common.validation.annotation.NotBlank
        String name;
        @mayfly.common.validation.annotation.NotNull
        Integer sex;
        @mayfly.common.validation.annotation.NotBlank
        String password;
    }

    private static Validator validator = Validation.buildDefaultValidatorFactory().getValidator();

    private static ValidationHandler validationHandler = ValidationHandler.getInstance();

    public static <T> void validate(T obj) throws ParamValidErrorException{
        Set<ConstraintViolation<T>> constraintViolations = validator.validate(obj);
        List<String> list = new ArrayList<>();
        // 抛出检验异常
        if (constraintViolations.size() > 0) {
            for (ConstraintViolation constraintViolation : constraintViolations) {
                list.add(constraintViolation.getMessage());
            }
        }
        if (list.size() > 0) {
            throw new ParamValidErrorException("cuowu");
        }
    }

    public static void main(String[] args) {
        int size = 1000000;
        User user = new User();
        User2 user2 = new User2();

        long start = System.currentTimeMillis();
        for (int i = 0; i < size; i++) {
            try {
                validationHandler.validate(user2);
//                validate(user);
            } catch (ParamValidErrorException e){
//                System.out.println(e.getMessage());
            }
        }
        long end = System.currentTimeMillis();
        System.out.println("result:" + (end - start));
    }
}
