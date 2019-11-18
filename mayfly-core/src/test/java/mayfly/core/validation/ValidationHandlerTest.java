package mayfly.core.validation;

import mayfly.core.util.enums.ValueEnum;
import mayfly.core.validation.annotation.EnumValue;
import mayfly.core.validation.annotation.NotBlank;
import mayfly.core.validation.annotation.NotNull;
import mayfly.core.validation.annotation.Valid;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

public class ValidationHandlerTest {

    enum StringEnum implements ValueEnum<String> {
        S1("01"), S2("02");

        private String value;
        StringEnum(String value) {
            this.value = value;
        }

        @Override
        public String getValue() {
            return this.value;
        }
    }

    enum IntegerEnum implements ValueEnum<Integer> {
        I1(1), I2(2);

        private Integer value;

        IntegerEnum(Integer value) {
            this.value = value;
        }


        @Override
        public Integer getValue() {
            return this.value;
        }
    }

    static class User{
        @NotBlank
        private String username;

        @EnumValue(StringEnum.class)
        private String sEnum;

        @EnumValue(IntegerEnum.class)
        private Integer iEnum;
    }

    static class Product{
        @NotBlank
        private String name;

        @NotNull
        @Valid
        private User[] us;

        @Valid
        private List<Integer> ids;

        @Valid
        private int[] is;
    }

    @Test
    public void validate() {
        Product p = new Product(); p.name = "111";
        User u = new User(); u.username = "111"; u.iEnum = 2; u.sEnum = "021";
        User u2 = new User(); u2.username = "1";
        p.us = new User[]{u, u2};
        p.ids = Arrays.asList(1, 2);
        p.is = new int[]{3, 4};
        try {
            ValidationHandler.getInstance().validate(u);
        } catch (ParamValidErrorException e) {
            System.out.println(e.getMessage());
        }
    }
}