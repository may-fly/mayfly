package mayfly.core.validation.annotation.time;

import javax.validation.Constraint;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import javax.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.time.LocalDateTime;
import java.time.chrono.ChronoLocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * @author meilin.huang
 * @date 2020-07-09 1:51 下午
 */
@Target({ElementType.METHOD, ElementType.FIELD, ElementType.ANNOTATION_TYPE, ElementType.CONSTRUCTOR, ElementType.PARAMETER, ElementType.TYPE_USE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Constraint(validatedBy = DateTimeRange.DateTimeRangeValidator.class)
public @interface DateTimeRange {

    int min() default 0;

    int max() default Integer.MAX_VALUE;

    /**
     * 时间单位 (年月日周时分秒)
     */
    RangeUnit unit() default RangeUnit.DAYS;

    /**
     * 作用于字符串时，指定的格式，包含年月日时分秒
     */
    String pattern() default "yyyy-MM-dd HH:mm:ss";

    /**
     * 错误提示
     */
    String message() default "日期时间错误";

    /**
     * 用于分组校验
     */
    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};


    class DateTimeRangeValidator implements ConstraintValidator<DateTimeRange, Object> {

        private DateTimeRange dateTimeRange;

        @Override
        public void initialize(DateTimeRange dateRange) {
            this.dateTimeRange = dateRange;
        }

        @Override
        public boolean isValid(Object value, ConstraintValidatorContext context) {
            if (value == null) {
                return true;
            }
            LocalDateTime ta = getByValue(value);
            if (ta == null) {
                return false;
            }

            LocalDateTime now = LocalDateTime.now();
            RangeUnit unit = dateTimeRange.unit();
            return ta.isAfter((ChronoLocalDateTime<?>) RangeUnit.plus(now, unit, dateTimeRange.min()))
                    && ta.isBefore((ChronoLocalDateTime<?>) RangeUnit.plus(now, unit, dateTimeRange.max()));
        }

        private LocalDateTime getByValue(Object value) {
            if (value instanceof LocalDateTime) {
                return (LocalDateTime) value;
            }
            if (value instanceof String) {
                try {
                    return LocalDateTime.parse((String) value, DateTimeFormatter.ofPattern(dateTimeRange.pattern()));
                } catch (Exception e) {
                    return null;
                }
            }
            return null;
        }
    }
}
