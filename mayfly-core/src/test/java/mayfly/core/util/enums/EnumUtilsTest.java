package mayfly.core.util.enums;

import org.junit.Assert;
import org.junit.Test;

import static org.junit.Assert.*;

public class EnumUtilsTest {

    enum Test1 implements NameValueEnum<String> {
        T1("01", "String类型测试1"),
        T2("02", "String类型测试2");

        private String value;
        private String name;

        Test1(String value, String name) {
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

    enum Test2 implements NameValueEnum<Integer> {
        T1(1, "Integer类型测试1"),
        T2(2, "Integer类型测试2");

        private Integer value;
        private String name;

        Test2(Integer value, String name) {
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
        Assert.assertTrue(EnumUtils.isExist(Test1.values(), "01"));
        Assert.assertFalse(EnumUtils.isExist(Test1.values(), "03"));
        Assert.assertTrue(EnumUtils.isExist(Test2.values(), 2));
        Assert.assertFalse(EnumUtils.isExist(Test2.values(), 8));
    }

    @Test
    public void getNameByValue() {
        String name = EnumUtils.getNameByValue(Test1.values(), "01");
        String name2 = EnumUtils.getNameByValue(Test2.values(), 2);
        System.out.println(name);
        System.out.println(name2);
    }

    @Test
    public void getValueByName() {
        String value = EnumUtils.getValueByName(Test1.values(), "String类型测试1");
        Integer value2 = EnumUtils.getValueByName(Test2.values(), "Integer类型测试2");
        System.out.println(value);
        System.out.println(value2);
    }

    @Test
    public void getEnumByValue() {
        Test2 enumByValue = EnumUtils.getEnumByValue(Test2.values(), 1);
        Test1 test = EnumUtils.getEnumByValue(Test1.class, "02");
        System.out.println();
    }


}