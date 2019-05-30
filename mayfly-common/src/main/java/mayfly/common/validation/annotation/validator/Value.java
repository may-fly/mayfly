package mayfly.common.validation.annotation.validator;

/**
 * @author meilin.huang
 * @version 1.0
 * @date 2019-05-30 13:59
 */
public class Value<T> {

    private String name;

    private T value;

    private Value(String name, T value) {
        this.name = name;
        this.value = value;
    }

    public static <T> Value<T> of(String name, T value) {
        return new Value<T>(name, value);
    }

    public String getName() {
        return name;
    }

    public T getValue() {
        return value;
    }
}
