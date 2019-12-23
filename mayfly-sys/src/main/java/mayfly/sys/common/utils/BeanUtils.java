package mayfly.sys.common.utils;

import mayfly.core.util.CollectionUtils;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author meilin.huang
 * @version 1.0
 * @date 2018-11-23 6:17 PM
 */
public class BeanUtils extends mayfly.core.util.bean.BeanUtils {

    public static void copyProperties(Object source, Object target) {
        org.springframework.beans.BeanUtils.copyProperties(source, target);
    }

    public static <T> T copyProperties(Object source, Class<T> targetClass) {
        if (source == null) {
            return null;
        }
        T target;
        try {
            target = targetClass.newInstance();
            copyProperties(source, target);
        } catch (Exception e) {
            throw new IllegalArgumentException(e);
        }
        return target;
    }

    public static <T> List<T> copyProperties(List<?> source, Class<T> targetClass) {
        if (CollectionUtils.isEmpty(source)) {
            return Collections.emptyList();
        }
        return source.stream().map(s -> copyProperties(s, targetClass)).collect(Collectors.toList());
    }
}
