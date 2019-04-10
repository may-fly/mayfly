package mayfly.common.utils;

import java.lang.annotation.Annotation;
import java.lang.reflect.AnnotatedElement;

/**
 * @author meilin.huang
 * @version 1.0
 * @date 2019-04-09 16:27
 */
public final class AnnotationUtils {

    /**
     * 获取指定元素的注解类型（若没有直接注解，则从元素其他注解的元注解上查找）
     * @param annotatedElement   可以包含注解的元素，如Field, Method等
     * @param annotationType  注解类型
     * @param <A>
     * @return
     */
    public static <A extends Annotation> A getAnnotation(AnnotatedElement annotatedElement, Class<A> annotationType) {
        A annotation = annotatedElement.getAnnotation(annotationType);
        if (annotation == null) {
            for (Annotation metaAnn : annotatedElement.getAnnotations()) {
                annotation = findMetaAnnotation(metaAnn, annotationType);
                if (annotation != null) {
                    break;
                }
            }
        }
        return annotation;
    }

    /**
     * 根据元注解类型递归查找指定注解的元注解
     * @param annotation  注解
     * @param type   元注解类型
     * @param <A>
     * @return
     */
    private static <A extends Annotation> A findMetaAnnotation(Annotation annotation, Class<A> type) {
        A a = annotation.annotationType().getAnnotation(type);
        if (a == null) {
            for (Annotation metaAnn : annotation.annotationType().getAnnotations()) {
                Class<? extends Annotation> metaAnnType = metaAnn.annotationType();
                //如果是java自带的元注解，则跳过
                if (metaAnnType.getName().startsWith("java.lang.annotation")) {
                    continue;
                }
                //如果注解存在指定元注解，则跳出循环，否则，递归查找
                if ((a = metaAnnType.getAnnotation(type)) != null) {
                    break;
                }
                findMetaAnnotation(metaAnn, type);
            }
        }
        return a;
    }

    /**
     * 判断指定元素是否含有某个注解（若没有直接注解，则从元素其他注解的元注解上查找）
     * @param annotatedElement   可以包含注解的元素，如Field, Method等
     * @param annotationType  注解类型
     * @param <A>
     * @return
     */
    public static <A extends Annotation> boolean isAnnotationPresent(AnnotatedElement annotatedElement, Class<A> annotationType) {
        return getAnnotation(annotatedElement, annotationType) != null;
    }

}
