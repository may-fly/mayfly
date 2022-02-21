package mayfly.core.util.enums;

import org.junit.Assert;
import org.junit.Test;

public class EnumUtilsTest {

    enum TestStrEnum implements NameValueEnum<String> {
        T1("01", "String类型测试1"),
        T2("02", "String类型测试2");

        private final String value;
        private final String name;

        TestStrEnum(String value, String name) {
            this.value = value;
            this.name = name;
        }

        @Override
        public String getName() {
            return this.name;
        }

        @Override
        public String getValue() {
            return this.value;
        }
    }

    enum TestIntEnum implements NameValueEnum<Integer> {
        T1(1, "Integer类型测试1"),
        T2(2, "Integer类型测试2");

        private final Integer value;
        private final String name;

        TestIntEnum(Integer value, String name) {
            this.value = value;
            this.name = name;
        }

        @Override
        public String getName() {
            return this.name;
        }

        @Override
        public Integer getValue() {
            return this.value;
        }
    }

    @Test
    public void isExist() {
        Assert.assertTrue(EnumUtils.isExist(TestStrEnum.values(), "01"));
        Assert.assertFalse(EnumUtils.isExist(TestStrEnum.values(), "03"));
        Assert.assertTrue(EnumUtils.isExist(TestIntEnum.values(), 2));
        Assert.assertFalse(EnumUtils.isExist(TestIntEnum.values(), 8));
    }

    @Test
    public void getNameByValue() {
        String name = EnumUtils.getNameByValue(TestStrEnum.values(), "01");
        String name2 = EnumUtils.getNameByValue(TestIntEnum.values(), 2);
        System.out.println(name);
        System.out.println(name2);
    }

    @Test
    public void getValueByName() {
        String value = EnumUtils.getValueByName(TestStrEnum.values(), "String类型测试1");
        Integer value2 = EnumUtils.getValueByName(TestIntEnum.values(), "Integer类型测试2");
        System.out.println(value);
        System.out.println(value2);
    }

    @Test
    public void getEnumByValue() {
        TestIntEnum enumByValue = EnumUtils.getEnumByValue(TestIntEnum.values(), 1);
        TestStrEnum test = EnumUtils.getEnumByValue(TestStrEnum.class, "02");
    }
}