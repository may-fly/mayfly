package mayfly.sys.common.utils;

import lombok.Data;
import mayfly.common.util.BeanUtils;
import mayfly.common.util.DateUtils;
import mayfly.sys.common.enums.ResourceTypeEnum;
import org.springframework.core.annotation.AliasFor;
import org.springframework.core.annotation.AnnotatedElementUtils;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;
import java.time.LocalDateTime;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.PARAMETER;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * @author meilin.huang
 * @version 1.0
 * @date 2019-04-25 10:52
 */
@BeanUtilsTest.DateRange(pattern2 = "yyyymmdd")
public class BeanUtilsTest {

    @Data
    public static class User{
        @BeanUtils.Bean2MapFieldConverter(enumConverter = ResourceTypeEnum.class)
        private Integer type;

        @BeanUtils.Bean2MapFieldConverter(enumConverter = ResourceTypeEnum.class)
        private Integer type2;

        @BeanUtils.Bean2MapFieldConverter(enumConverter = ResourceTypeEnum.class)
        private Integer type3;

        @BeanUtils.Bean2MapFieldConverter(rename = "localDateTime", converter = DateConverter.class)
        private LocalDateTime date;

        private Integer sex;

        private String name;

        private String email;

        public static class DateConverter implements BeanUtils.FieldValueConverter<LocalDateTime, String> {
            @Override
            public String convert(LocalDateTime fieldValue) {
                return DateUtils.defaultFormat(fieldValue);
            }
        }
    }

    @Target({ FIELD, PARAMETER, ElementType.TYPE})
    @Retention(RUNTIME)
    @Documented
    @interface DatePattern {
        String pattern() default "yyyy-mm-dd";
    }

    @Target({ FIELD, PARAMETER, ElementType.TYPE})
    @Retention(RUNTIME)
    @Documented
    @DatePattern
    @interface DateRange {

        @AliasFor(attribute = "pattern", annotation = DatePattern.class)
        String pattern2() default "";
    }

    public static void main(String[] args) {
//        int size = 100000;
//        User u = new User();
//        u.type = 1;
//        u.type2 = 2;
//        u.type3 = 1;
//        u.date = LocalDateTime.now();
//        u.sex = 1;
//        u.name = "haha";
//        u.email = "slsjfl";
//
//        long start = System.currentTimeMillis();
//        for (int i = 0; i < size; i++) {
//            mayfly.sys.common.util.BeanUtils.beans2Maps(Arrays.asList(u, u));
//        }
//        long end = System.currentTimeMillis();
//        System.out.println(end - start);
        System.out.println(AnnotatedElementUtils.getMergedAnnotation(BeanUtilsTest.class, DatePattern.class).pattern());
    }
}
