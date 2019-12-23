package mayfly.core.util.bean;

import mayfly.core.util.enums.NameValueEnum;
import org.junit.Test;

import java.util.List;

public class BeanUtilsTest {

    public enum TestEnum implements NameValueEnum<Integer> {
        /**
         * false
         */
        FALSE(0, "tset0"),

        /**
         * true
         */
        TRUE(1, "test2");

        /**
         * 状态值
         */
        private Integer value;

        private String name;

        TestEnum(Integer value, String name) {
            this.value = value;
            this.name = name;
        }

        @Override
        public Integer getValue() {
            return this.value;
        }

        @Override
        public String getName() {
            return this.name;
        }
    }

    static class User {
        @FieldValueConvert(enumConverter = TestEnum.class)
        private Integer id;

        private String name;

        private String pass;

        public User(Integer id, String name, String pass) {
            this.id = id;
            this.name = name;
            this.pass = pass;
        }
    }

    @Test
    public void checkFieldChange() {
        User old = new User(0, "haha", "hehe2");
        User newO = new User(1, "haha11", "hehe");

        long s = System.currentTimeMillis();
        for (int i = 0; i < 1; i++) {
            List<FieldValueChangeRecord> fieldValueChangeRecords = BeanUtils.getFieldValueChangeRecords(newO, old);
            System.out.println(fieldValueChangeRecords);
        }
        System.out.println(System.currentTimeMillis() - s);
    }
}