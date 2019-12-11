package mayfly.sys.common.utils;

import mayfly.core.log.MethodLog;
import mayfly.core.util.annotation.OverrideFor;
import mayfly.core.validation.annotation.DateRange;
import org.springframework.core.annotation.AliasFor;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * @author meilin.huang
 * @version 1.0
 * @date 2019-05-09 11:19
 */
//@MethodLog
@Test5Ann(test4 = "测试5蛤", test3 = "测试53")
public class Test {

//    public static void main(String[] args) {
//        long start = System.currentTimeMillis();
//        for (int i = 0; i < 3; i++) {
////            Test4Ann anno = AnnotationUtils.getAnnotation(Test.class, Test4Ann.class);
////            TestAnn anno = AnnotatedElementUtils.getMergedAnnotation(Test.class, TestAnn.class);
////            System.out.println(AnnotatedElementUtils.getMergedAnnotation(Test.class, Test2Ann.class).test22());
//            System.out.println(AnnotationUtils.getAnnotation(Test.class, Test2Ann.class).test2());
//        }
//        long end = System.currentTimeMillis();
//        System.out.println(end - start);
////        Test2Ann anno2 = AnnotationUtils.getAnnotation(Test.class, Test2Ann.class);
////        Test3Ann anno3 = AnnotationUtils.getAnnotation(Test.class, Test3Ann.class);
////        Test4Ann anno4 = AnnotationUtils.getAnnotation(Test.class, Test4Ann.class);
////        Test5Ann anno5 = AnnotationUtils.getAnnotation(Test.class, Test5Ann.class);
////        System.out.println(AnnotationUtils.getValue(anno5, "test5"));
////        System.out.println(pattern.regexp());
////        System.out.println(AnnotatedElementUtils.getMergedAnnotation(Test.class, Test3Ann.class).test3());
////        System.out.println(AnnotationUtils.getAnnotation(Test.class, Test4Ann.class).test42());
//    }
}

@DateRange
@Documented
@Target({ANNOTATION_TYPE, FIELD, TYPE})
@Retention(RUNTIME)
@Test2Ann
@interface TestAnn {
    String test1() default "test1";

    @AliasFor(annotation = Test2Ann.class, attribute = "test22")
    @OverrideFor(annotation = Test2Ann.class, attribute = "test22")
    String test() default "test";
}

@DateRange
@Documented
@Target({ANNOTATION_TYPE, FIELD, TYPE})
@Retention(RUNTIME)
@TestAnn
@interface Test2Ann {
    String test2() default "test2";

    String test22() default "test22";
}

@DateRange
@MethodLog
@Documented
@Target({ANNOTATION_TYPE, FIELD, TYPE})
@Retention(RUNTIME)
@Test2Ann
@interface Test3Ann {
    String test3() default "test3";

    @AliasFor(annotation = Test2Ann.class, attribute = "test2")
    @OverrideFor(annotation = Test2Ann.class, attribute = "test2")
    String test32() default "test32";

    //    @OverrideFor(annotation = Test2Ann.class, attribute = "test22")
    String test33() default "test33";
}

@DateRange
@MethodLog
@Documented
@Target({ANNOTATION_TYPE, FIELD, TYPE})
@Retention(RUNTIME)
@Test3Ann
@TestAnn
@interface Test4Ann {
    @OverrideFor(attribute = "test", annotation = TestAnn.class)
    @AliasFor(attribute = "test", annotation = TestAnn.class)
    String test4() default "test4";


    @AliasFor(annotation = Test3Ann.class, attribute = "test32")
    @OverrideFor(annotation = Test3Ann.class, attribute = "test32")
    String test42() default "test42";
}

@DateRange
@MethodLog
@Documented
@Target({ANNOTATION_TYPE, FIELD, TYPE})
@Retention(RUNTIME)
@Test4Ann(test42 = "hahahah42")
@interface Test5Ann {
    @AliasFor(annotation = Test4Ann.class, attribute = "test42")
    @OverrideFor(annotation = Test4Ann.class, attribute = "test42")
    String test4() default "test5";

    @OverrideFor(annotation = MethodLog.class, attribute = "value")
    @AliasFor(annotation = Test4Ann.class, attribute = "test4")
    String test3() default "test3";
}