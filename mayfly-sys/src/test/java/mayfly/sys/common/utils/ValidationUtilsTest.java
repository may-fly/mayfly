package mayfly.sys.common.utils;

import mayfly.core.util.enums.ValueEnum;
import mayfly.core.validation.ValidationResult;
import mayfly.core.validation.ValidatorUtils;
import mayfly.core.validation.annotation.EnumValue;
import mayfly.sys.module.sys.enums.ResourceTypeEnum;
import org.junit.Test;

/**
 * @author meilin.huang
 * @date 2020-04-14 12:25 下午
 */
public class ValidationUtilsTest {

    enum TestEnum implements ValueEnum<Integer> {
        T1(1);

        TestEnum(Integer value) {
            this.value = value;
        }
        private final Integer value;

        @Override
        public Integer getValue() {
            return this.value;
        }
    }

    static class User {
        String name;

        @EnumValue(value = ResourceTypeEnum.class, message = "资源类型错误，可选值为【{enums}】")
        Integer type;

        @EnumValue(TestEnum.class)
        Integer type2;
    }

    @Test
    public void testValidate() {
        User user = new User();
        user.type = 4;
        user.type2 = 2;
        ValidationResult res = ValidatorUtils.validate(user);
        System.out.println(res);
    }
}
