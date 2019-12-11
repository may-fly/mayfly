package mayfly.core.util.annotation;

import org.junit.Test;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.PARAMETER;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * @author meilin.huang
 * @version 1.0
 * @date 2019-11-25 1:22 下午
 */
@AnnotationTest.ATest("test")
public class AnnotationTest {

    static class TestC {
        static {
            System.out.println("init test c");
        }
    }

    @Target({FIELD, PARAMETER, ElementType.TYPE})
    @Retention(RUNTIME)
    @Documented
    public @interface ATest {
        String value();
    }

    @Test
    public void testDynamic() throws Exception {
        System.out.println(AnnotationUtils.getAnnotation(AnnotationTest.class, ATest.class).value());
    }

}
