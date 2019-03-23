package mayfly.sys.common.utils;

/**
 * @author meilin.huang
 * @version 1.0
 * @date 2018-11-23 6:17 PM
 */
public class BeanUtils {


    public static<T> T copyProperties(Object source, Class<T> targetClass) {
        if (source == null) {
            return null;
        }
        T target;
        try {
            target = targetClass.newInstance();
            org.springframework.beans.BeanUtils.copyProperties(source, target);
        } catch (Exception e) {
            throw new IllegalArgumentException(e);
        }
        return target;
    }
}
