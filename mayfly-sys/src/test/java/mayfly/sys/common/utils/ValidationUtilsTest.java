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
        T1(1),

        T2(2);

        TestEnum(Integer value) {
            this.value = value;
        }

        private final Integer value;

        @Override
        public Integer getValue() {
            return this.value;
        }
    }

    enum TestEnum2 implements ValueEnum<String> {
        T1("T1"),

        T2("T2");

        private final String value;

        TestEnum2(String value) {
            this.value = value;
        }

        @Override
        public String getValue() {
            return this.value;
        }
    }

    static class User {
        String name;

        @EnumValue(value = ResourceTypeEnum.class)
        Integer type;

        @EnumValue(value = TestEnum.class, name = "type2", values = {"2"})
        Integer type2;

        @EnumValue(value = TestEnum2.class, values = {"T2"})
        String strType;

        @EnumValue(values = {"2"}, name="iteype")
        Integer iType;
    }

    @Test
    public void testValidate() {
        User user = new User();
        user.type = 1;
        user.type2 = 1;
        user.strType = "T1";
        user.iType = 2;
        ValidationResult res = ValidatorUtils.validate(user);
        System.out.println(res);
    }
}
