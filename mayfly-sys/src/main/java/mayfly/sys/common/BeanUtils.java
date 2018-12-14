package mayfly.sys.common;

/**
 * @author meilin.huang
 * @version 1.0
 * @date 2018-11-23 6:17 PM
 */
public class BeanUtils {


    public static<T> T copyProperties(Object from, Class<T> toClass) {
        if (from == null) {
            return null;
        }
        T to = null;
        try {
            to = toClass.newInstance();
            org.springframework.beans.BeanUtils.copyProperties(from, to);
        } catch (Exception e) {
            //skip
        }

        return to;
    }
}
