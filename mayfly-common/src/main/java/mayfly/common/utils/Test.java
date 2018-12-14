package mayfly.common.utils;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

/**
 * @Description:
 * @author: hml
 * @date: 2018/6/11 上午10:13
 */
public class Test {

    public static void test() throws Exception{
        Class<?> clazz = PatternUtils.class;
        Class clazz2 = PatternUtils.class;
        System.out.println("class equals : " + (clazz == clazz2));
        Object obj = clazz.newInstance();
        Field field = clazz.getDeclaredField("i");
        Field field1 = clazz.getDeclaredField("i");
        System.out.println("field equals : " + (field==field1));
        field.setAccessible(true);
        field.set(obj, 1);
        System.out.println(field.get(obj));
        System.out.println(Modifier.isPrivate(field.getModifiers()));
    }


    public static void test2() throws Exception{
        Class<?> clazz = PatternUtils.class;
        Object obj = clazz.newInstance();
        Field field = clazz.getDeclaredField("i");
        field.setAccessible(true);
        field.set(obj, 11);
        System.out.println(field.get(obj));
        System.out.println(Modifier.isPrivate(field.getModifiers()));
    }


//    public static void main(String[] args) throws Exception {
//        test();
//        test2();
//    }
}
