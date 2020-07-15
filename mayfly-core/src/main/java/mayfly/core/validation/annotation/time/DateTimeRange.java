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
 * 日期时间范围校验注解，可作用于LocalDateTime or 字符串型年月日时分秒（格式可通过pattern属性指定）
 *
 * @author meilin.huang
 * @date 2020-07-09 1:51 下午
 */
@Target({ElementType.METHOD, ElementType.FIELD, ElementType.ANNOTATION_TYPE, ElementType.CONSTRUCTOR, ElementType.PARAMETER, ElementType.TYPE_USE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Constraint(validatedBy = DateTimeRange.DateTimeRangeValidator.class)
public @interface DateTimeRange {

    /**
     * 最小时间范围（为负数即为前n unit）
     */
    int min() default 0;

    int max() default Integer.MAX_VALUE;

    /**
     * 时间单位 (年月日)
     */
    RangeUnit unit() default RangeUnit.DAYS;

    /**
     * 作用于字符串时，指定的格式，包含年月日时分秒
     */
    String pattern() default "yyyy-MM-dd HH:mm:ss";

    /**
     * 是否忽略更小的单位，即比当前指定的unit更小的单位（如unit为Days，则忽略hours，minutes, second）
     */
    boolean ignoreLowerUnit() default false;

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

            RangeUnit unit = dateTimeRange.unit();
            int min = dateTimeRange.min();
            int max = dateTimeRange.max();

            // 忽略更小单位时，计算两个时间的单位差值比较即可
            if (dateTimeRange.ignoreLowerUnit()) {
                long between = RangeUnit.getBetween(dateTimeRange.unit(), LocalDateTime.now(), ta);
                return between >= min && between <= max;
            }

            LocalDateTime now = LocalDateTime.now();
            return ta.isAfter((ChronoLocalDateTime<?>) RangeUnit.plus(now, unit, min))
                    && ta.isBefore((ChronoLocalDateTime<?>) RangeUnit.plus(now, unit, max));
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
