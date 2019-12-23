package mayfly.core.util.bean;

import mayfly.core.util.enums.NameValueEnum;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * bean属性转换器注解（主要用于导出字段值转换等场景）  </br>
 *
 * @author meilin.huang
 * @version 1.0
 * @date 2019-03-06 6:42 PM
 */
@Target({FIELD})
@Retention(RUNTIME)
public @interface FieldValueConvert {
    /**
     * 重命名
     */
    String rename() default "";

    /**
     * 值转换器
     */
    Class<? extends FieldValueConverter> converter() default FieldValueConverter.class;

    /**
     * 枚举值转换,枚举类必须继承EnumValue接口
     */
    Class<? extends Enum<? extends NameValueEnum>> enumConverter() default DefaultEnum.class;

    enum DefaultEnum implements NameValueEnum<Integer> {
        ;

        @Override
        public Integer getValue() {
            return 0;
        }

        @Override
        public String getName() {
            return null;
        }
    }
}
