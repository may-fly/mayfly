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
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.chrono.ChronoLocalDate;
import java.time.format.DateTimeFormatter;

/**
 * 作用于 {@link LocalDate} or 字符串（年月日格式，默认格式为: yyyy-MM-dd）
 *
 * @author meilin.huang
 * @date 2020-07-08 5:15 下午
 */
@Target({ElementType.METHOD, ElementType.FIELD, ElementType.ANNOTATION_TYPE, ElementType.CONSTRUCTOR, ElementType.PARAMETER, ElementType.TYPE_USE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Constraint(validatedBy = DateRange.DateRangeValidator.class)
public @interface DateRange {

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
     * 作用于字符串时，指定的格式，只能包含年月日不包含时间
     */
    String pattern() default "yyyy-MM-dd";

    /**
     * 是否忽略更小的单位，即比当前指定的unit更小的单位（如unit为Month，则忽略Days）
     */
    boolean ignoreLowerUnit() default false;

    /**
     * 错误提示
     */
    String message() default "日期错误";

    /**
     * 用于分组校验
     */
    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};


    class DateRangeValidator implements ConstraintValidator<DateRange, Object> {

        private DateRange dateRange;

        @Override
        public void initialize(DateRange dateRange) {
            this.dateRange = dateRange;
        }

        @Override
        public boolean isValid(Object value, ConstraintValidatorContext context) {
            if (value == null) {
                return true;
            }
            LocalDate ta = getByValue(value);
            if (ta == null) {
                return false;
            }
            // 忽略更小单位时，计算两个时间的单位差值比较即可
            if (dateRange.ignoreLowerUnit()) {
                long between = RangeUnit.getBetween(dateRange.unit(), LocalDate.now(), ta);
                return between >= dateRange.min() && between <= dateRange.max();
            }

            LocalDate now = LocalDate.now();
            RangeUnit unit = dateRange.unit();
            ChronoLocalDate min = (ChronoLocalDate) RangeUnit.plus(now, unit, dateRange.min());
            ChronoLocalDate max = (ChronoLocalDate) RangeUnit.plus(now, unit, dateRange.max());
            return (ta.isAfter(min) || ta.isEqual(min))
                    && (ta.isBefore(max) || ta.isEqual(max));
        }

        private LocalDate getByValue(Object value) {
            if (value instanceof LocalDate) {
                return (LocalDate) value;
            }
            if (value instanceof String) {
                try {
                    return LocalDate.parse((String) value, DateTimeFormatter.ofPattern(dateRange.pattern()));
                } catch (Exception e) {
                    return null;
                }
            }
            if (value instanceof LocalDateTime) {
                return ((LocalDateTime) value).toLocalDate();
            }
            return null;
        }
    }
}
