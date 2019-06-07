package mayfly.common.utils.annotation;

import mayfly.common.utils.ReflectionUtils;
import mayfly.common.utils.StringUtils;

import java.lang.annotation.Annotation;
import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author meilin.huang
 * @version 1.0
 * @date 2019-04-09 16:27
 */
public final class AnnotationUtils {

    /**
     * 组合注解属性值提取器缓存
     */
    private static Map<SynthesizedAnnotationCacheKey, AttributeValueExtractor> synthesizedCache = new ConcurrentHashMap<>(256);

    /**
     * 没有目标注解作用的元素, synthesizedCache的value都指向该对象
     */
    private static final DefaultAttributeExtractor NO_PRESENT = new DefaultAttributeExtractor();


    /**
     * 获取指定元素的注解类型（若没有直接注解，则从元素其他注解的元注解上查找）
     * @param annotatedElement   可以包含注解的元素，如Field, Method等
     * @param annotationType  注解类型
     * @param <A>
     * @return
     */
    public static <A extends Annotation> A getAnnotation(AnnotatedElement annotatedElement, Class<A> annotationType) {
        A annotation = annotatedElement.getAnnotation(annotationType);
        // 如果元素上含有直接注解，则返回
        if (annotation != null) {
            return annotation;
        }

        SynthesizedAnnotationCacheKey key = SynthesizedAnnotationCacheKey.of(annotatedElement, annotationType);
        AttributeValueExtractor valueExtractor = synthesizedCache.get(key);
        if (valueExtractor == NO_PRESENT) {
            return null;
        }
        // 如果为空，则遍历递归查找该元素其他注解的元注解
        if (valueExtractor == null) {
            // 元注解访问链
            List<Annotation> visited = new ArrayList<>();
            // 遍历查找该元素的其他注解上的元注解
            for (Annotation otherAnn : annotatedElement.getAnnotations()) {
                annotation = findMetaAnnotation(otherAnn, annotationType, visited);
                // 如果从该元注解没有找到指定注解，则清空该访问链，继续该元素其他注解上查找
                if (annotation == null) {
                    visited.clear();
                    continue;
                }
                break;
            }
            // 如果注解不存在，则将该组合key的value置为不存在标识
            if (annotation == null) {
                synthesizedCache.put(key, NO_PRESENT);
                return null;
            }
            // 生成对应的组合注解信息，并缓存
            valueExtractor = DefaultAttributeExtractor.from(annotation, visited);
            synthesizedCache.put(key, valueExtractor);
        }

        return annotation != null ? synthesizeAnnotation(annotatedElement, annotation, valueExtractor) : null;
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

    /**
     * 根据元注解类型递归查找指定注解的元注解
     * @param annotation  注解
     * @param targetType   元注解类型
     * @param <A>
     * @return
     */
    private static <A extends Annotation> A findMetaAnnotation(Annotation annotation, Class<A> targetType, List<Annotation> visited) {
        // 添加注解至访问链
        visited.add(annotation);
        A a = annotation.annotationType().getAnnotation(targetType);
        if (a == null) {
            for (Annotation metaAnn : annotation.annotationType().getAnnotations()) {
                Class<? extends Annotation> metaAnnType = metaAnn.annotationType();
                // 如果是java自带的元注解，则跳过继续
                if (metaAnnType.getName().startsWith("java.lang.annotation")) {
                    continue;
                }
                // 如果注解存在指定元注解，则跳出循环，否则，递归查找
                if ((a = metaAnnType.getAnnotation(targetType)) != null) {
                    visited.add(metaAnn);
                    break;
                }
                // 递归查找
                if ((a = findMetaAnnotation(metaAnn, targetType, visited)) != null) {
                    break;
                }
                // 没有找到则移除访问链中最后一个无用的访问
                visited.remove(visited.size() - 1);
            }
        }

        return a;
    }

    /**
     * 将注解使用动态代理包装，以实现组合注解功能
     * @param annotatedElement  注解作用的元素
     * @param valueExtractor 属性值提取器
     * @param <A>
     * @return
     */
    @SuppressWarnings("unchecked")
    private static <A extends Annotation> A synthesizeAnnotation(AnnotatedElement annotatedElement, Annotation annotation, AttributeValueExtractor valueExtractor) {
        Class<? extends Annotation> annotationType = annotation.annotationType();
        // 组合注解代理处理器
        InvocationHandler handler = new SynthesizedAnnotationInvocationHandler(valueExtractor);

        Class<?>[] exposedInterfaces = new Class<?>[] {annotationType};
        return (A) Proxy.newProxyInstance(annotation.getClass().getClassLoader(), exposedInterfaces, handler);
    }

    /**
     * 获取指定注解上的所有属性方法含有的别名属性描述
     * @param a
     * @return
     */
    private static List<AliasDescriptor> getAliasDescriptors(Annotation a) {
        List<AliasDescriptor> descriptors = null;
        for (Method attribute : getAttributeMethods(a.annotationType())) {
            AliasDescriptor des = AliasDescriptor.from(attribute);
            if (des != null) {
                if (descriptors == null) {
                    descriptors = new ArrayList<>(8);
                }
                descriptors.add(des);
            }
        }
        return Optional.ofNullable(descriptors).orElse(Collections.emptyList());
    }

    /**
     * 获取注解中的所有属性方法
     * @param annotationType  注解类型
     * @return 属性方法列表
     */
    private static List<Method> getAttributeMethods(Class<? extends Annotation> annotationType) {
        List<Method> methods = new ArrayList<>();
        for (Method method : annotationType.getDeclaredMethods()) {
            if (isAttributeMethod(method)) {
                ReflectionUtils.makeAccessible(method);
                methods.add(method);
            }
        }
        return methods;
    }

    /**
     * 判断方法是否为注解的属性方法
     * @param method  方法
     * @return
     */
    private static boolean isAttributeMethod(Method method) {
        return (method != null && method.getParameterCount() == 0 && method.getReturnType() != void.class);
    }

    /**
     * 获取注解的属性值
     * @param annotation  注解对象
     * @param attributeName  属性值（即方法名）
     * @return
     */
    public static Object getValue(Annotation annotation, String attributeName) {
        if (annotation == null || StringUtils.isEmpty(attributeName)) {
            return null;
        }
        try {
            Method method = annotation.annotationType().getDeclaredMethod(attributeName);
            ReflectionUtils.makeAccessible(method);
            return method.invoke(annotation);
        } catch (Exception ex) {
            String msg = String.format("%s注解无法获取%s属性值！", annotation.annotationType().getName(), attributeName);
            throw new IllegalArgumentException(msg, ex);
        }
    }



    /**
     * 属性值获取器
     */
    interface AttributeValueExtractor {
        /**
         * 根据属性名获取对应的属性值
         * @param attribute  属性名（注解方法名）
         * @return  属性值
         */
        Object getAttributeValue(String attribute);

        /**
         * 获取注解
         * @return  目标注解
         */
       Annotation getAnnotation();
    }

    /**
     * 默认属性值提取器（将属性值和属性名存于map，并从中获取对应的属性值）
     */
    private static class DefaultAttributeExtractor implements AttributeValueExtractor{
        /**
         * 目标注解
         */
        private Annotation annotation;

        /**
         * 注解属性 key:属性名（即注解中的方法名） value:属性值
         */
        private Map<String, Object> attributes;

        private DefaultAttributeExtractor() {}

        private DefaultAttributeExtractor(Annotation annotation, Map<String, Object> attributes) {
            this.annotation = annotation;
            this.attributes = attributes;
        }

        /**
         * 生成默认的属性值提取器
         * @param target 目标注解
         * @param visited 目标注解的访问路径
         * @return 默认属性值提取器
         */
        private static DefaultAttributeExtractor from(Annotation target, List<Annotation> visited) {
            Class<? extends Annotation> targetType = target.annotationType();
            Map<String, Object> attributes = new LinkedHashMap<>(8);
            for (Annotation a : visited) {
                List<AliasDescriptor> aliasDescriptors = getAliasDescriptors(a);
                if (aliasDescriptors.isEmpty()) {
                    continue;
                }
                // 遍历该注解上的所有别名描述器，并判断目标注解的属性是否有被其他注解当做别名属性使用
                for (AliasDescriptor descriptor : aliasDescriptors) {
                    if (descriptor.aliasedAnnotationType == targetType) {
                        String targetAttributeName = descriptor.aliasedAttributeName;
                        if (!attributes.containsKey(targetAttributeName)) {
                            attributes.put(targetAttributeName, ReflectionUtils.invokeMethod(descriptor.sourceAttribute, a));
                        }
                    }
                }
            }
            // 判断哪些属性没有被覆盖，没覆盖则补上对应的属性值
            for (Method targetAttribute : getAttributeMethods(targetType)) {
                String attributeName = targetAttribute.getName();
                if (!attributes.containsKey(attributeName)) {
                    attributes.put(attributeName, ReflectionUtils.invokeMethod(targetAttribute, target));
                }
            }
            return new DefaultAttributeExtractor(target, attributes);
        }

        @Override
        public Object getAttributeValue(String attribute) {
            return this.attributes.get(attribute);
        }

        @Override
        public Annotation getAnnotation() {
            return this.annotation;
        }
    }

    /**
     * 别名描述器，用于描述{@link mayfly.common.utils.annotation.Alias}细节
     */
    public static class AliasDescriptor {
        /**
         * 源注解属性方法
         */
        private final Method sourceAttribute;

        /**
         * 源注解类型
         */
        private final Class<? extends Annotation> sourceAnnotationType;

        /**
         * 源注解属性名（即方法名）
         */
        private final String sourceAttributeName;

        /**
         * 别名的注解属性方法
         */
        private final Method aliasedAttribute;

        /**
         * 别名的注解类型
         */
        private final Class<? extends Annotation> aliasedAnnotationType;

        /**
         * 别名的属性名
         */
        private final String aliasedAttributeName;

        /**
         * 别名是否为本身其他属性方法
         */
        private final boolean isAliasPair;

        public static AliasDescriptor from(Method attribute) {
            Alias alias = attribute.getAnnotation(Alias.class);
            if (alias == null) {
                return null;
            }

            AliasDescriptor descriptor = new AliasDescriptor(attribute, alias);
            return descriptor;
        }

        @SuppressWarnings("unchecked")
        private AliasDescriptor(Method sourceAttribute, Alias alias) {
            Class<?> declaringClass = sourceAttribute.getDeclaringClass();

            this.sourceAttribute = sourceAttribute;
            this.sourceAnnotationType = (Class<? extends Annotation>) declaringClass;
            this.sourceAttributeName = sourceAttribute.getName();

            this.aliasedAnnotationType = (Annotation.class == alias.annotation() ?
                    this.sourceAnnotationType : alias.annotation());
            this.aliasedAttributeName = getAliasedAttributeName(alias, sourceAttribute);
            if (this.aliasedAnnotationType == this.sourceAnnotationType &&
                    this.aliasedAttributeName.equals(this.sourceAttributeName)) {
                String msg = String.format("@%s注解的%s属性方法上的@Alias的别名注解类型和属性名不能与该方法一致!",
                        declaringClass.getName(), sourceAttribute.getName());
                throw new AnnotationConfigurationException(msg);
            }
            try {
                this.aliasedAttribute = this.aliasedAnnotationType.getDeclaredMethod(this.aliasedAttributeName);
            } catch (NoSuchMethodException ex) {
                String msg = String.format(
                        "@%s注解中%s属性方法上@Alias注解对应的%s别名属性不存在于%s别名注解类中！",
                        this.sourceAnnotationType.getName(), this.sourceAttributeName, this.aliasedAttributeName,
                        this.aliasedAnnotationType.getName());
                throw new AnnotationConfigurationException(msg, ex);
            }
            // 别名注解类型必修是源注解的元注解
            if (!sourceAnnotationType.isAnnotationPresent(aliasedAnnotationType)) {
                String msg = String.format("'@%s'注解的'%s'属性方法上的@Alias所声明的annotation别名注解类型不是该注解的元注解!",
                        sourceAnnotationType.getName(), sourceAttributeName);
                throw new IllegalArgumentException(msg);
            }
            // 判断别名属性方法与源属性方法返回值是否一致
            if (sourceAttribute.getReturnType() != aliasedAttribute.getReturnType()) {
                String msg = String.format("'@%s'注解的'%s'属性方法类型与该方法上@Alias所声明的'@%s'注解类型中的'%s'属性方法类型不匹配!",
                        sourceAnnotationType.getName(), sourceAttributeName, aliasedAnnotationType.getName(), aliasedAttributeName);
                throw new IllegalArgumentException(msg);
            }
            this.isAliasPair = (this.sourceAnnotationType == this.aliasedAnnotationType);
        }

        /**
         * 获取属性方法上的别名
         * @param alias  别名注解
         * @param sourceAttribute  注解属性方法
         * @return 如果@Alias注解的attribute和value属性值都为空，则反回sourceAttribute的方法名
         */
        private String getAliasedAttributeName(Alias alias, Method sourceAttribute) {
            String attribute = alias.attribute();
            String value = alias.value();
            boolean hasAttribute = !StringUtils.isEmpty(attribute);
            boolean hasValue = !StringUtils.isEmpty(value);
            if (hasAttribute && hasValue) {
                String msg = String.format("%s注解中的%s属性方法上的@Alias注解的value属性和attribute不能同时存在！",
                        sourceAttribute.getDeclaringClass().getName(), sourceAttribute.getName());
                throw new AnnotationConfigurationException(msg);
            }

            attribute = hasAttribute ? attribute : value;
            return !StringUtils.isEmpty(attribute) ? attribute : sourceAttribute.getName();
        }
    }

    /**
     * 组合注解代理处理器
     */
    static public class SynthesizedAnnotationInvocationHandler implements InvocationHandler {

        /**
         * 属性值提取器
         */
        private final AttributeValueExtractor attributeValueExtractor;

        SynthesizedAnnotationInvocationHandler(AttributeValueExtractor valueExtractor) {
            this.attributeValueExtractor = valueExtractor;
        }

        @Override
        public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
            if (ReflectionUtils.isEqualsMethod(method)) {
                return annotationEquals(args[0]);
            }
            if (ReflectionUtils.isHashCodeMethod(method)) {
                return annotationHashCode();
            }
            if (ReflectionUtils.isToStringMethod(method)) {
                return annotationToString();
            }
            if (method != null && method.getName().equals("annotationType") && method.getParameterCount() == 0) {
                return annotationType();
            }
            return attributeValueExtractor.getAttributeValue(method.getName());
        }

        private Object getAttributeValue(Method attributeMethod) {
            return attributeValueExtractor.getAttributeValue(attributeMethod.getName());
        }

        private Object cloneArray(Object array) {
            if (array instanceof boolean[]) {
                return ((boolean[]) array).clone();
            }
            if (array instanceof byte[]) {
                return ((byte[]) array).clone();
            }
            if (array instanceof char[]) {
                return ((char[]) array).clone();
            }
            if (array instanceof double[]) {
                return ((double[]) array).clone();
            }
            if (array instanceof float[]) {
                return ((float[]) array).clone();
            }
            if (array instanceof int[]) {
                return ((int[]) array).clone();
            }
            if (array instanceof long[]) {
                return ((long[]) array).clone();
            }
            if (array instanceof short[]) {
                return ((short[]) array).clone();
            }

            // else
            return ((Object[]) array).clone();
        }

        private Class<? extends Annotation> annotationType() {
            return attributeValueExtractor.getAnnotation().annotationType();
        }

        /**
         * 代理组合注解equals方法 {@link Annotation#equals(Object)}
         */
        private boolean annotationEquals(Object other) {
            if (this == other) {
                return true;
            }
            if (!annotationType().isInstance(other)) {
                return false;
            }

            for (Method attributeMethod : AnnotationUtils.getAttributeMethods(annotationType())) {
                Object thisValue = getAttributeValue(attributeMethod);
                Object otherValue = ReflectionUtils.invokeMethod(attributeMethod, other);
                if (!Objects.equals(thisValue, otherValue)) {
                    return false;
                }
            }

            return true;
        }

        /**
         * 代理组合注解hashCode方法 {@link Annotation#hashCode()}
         */
        private int annotationHashCode() {
            int result = 0;

            for (Method attributeMethod : AnnotationUtils.getAttributeMethods(annotationType())) {
                Object value = getAttributeValue(attributeMethod);
                int hashCode;
                if (value.getClass().isArray()) {
                    hashCode = hashCodeForArray(value);
                }
                else {
                    hashCode = value.hashCode();
                }
                result += (127 * attributeMethod.getName().hashCode()) ^ hashCode;
            }

            return result;
        }

        private int hashCodeForArray(Object array) {
            if (array instanceof boolean[]) {
                return Arrays.hashCode((boolean[]) array);
            }
            if (array instanceof byte[]) {
                return Arrays.hashCode((byte[]) array);
            }
            if (array instanceof char[]) {
                return Arrays.hashCode((char[]) array);
            }
            if (array instanceof double[]) {
                return Arrays.hashCode((double[]) array);
            }
            if (array instanceof float[]) {
                return Arrays.hashCode((float[]) array);
            }
            if (array instanceof int[]) {
                return Arrays.hashCode((int[]) array);
            }
            if (array instanceof long[]) {
                return Arrays.hashCode((long[]) array);
            }
            if (array instanceof short[]) {
                return Arrays.hashCode((short[]) array);
            }

            // else
            return Arrays.hashCode((Object[]) array);
        }

        /**
         *  代理组合注解toString方法
         */
        private String annotationToString() {
            StringBuilder sb = new StringBuilder("@").append(annotationType().getName()).append("(");

            Iterator<Method> iterator = AnnotationUtils.getAttributeMethods(annotationType()).iterator();
            while (iterator.hasNext()) {
                Method attributeMethod = iterator.next();
                sb.append(attributeMethod.getName());
                sb.append('=');
                sb.append(attributeValueToString(getAttributeValue(attributeMethod)));
                sb.append(iterator.hasNext() ? ", " : "");
            }

            return sb.append(")").toString();
        }

        private String attributeValueToString(Object value) {
            return String.valueOf(value);
        }
    }

    /**
     * 组合注解缓存key
     */
    private static class SynthesizedAnnotationCacheKey {
        /**
         * 注解作用元素
         */
        private AnnotatedElement element;

        /**
         * 目标注解类
         */
        private Class<? extends Annotation> targetAnnotation;

        private SynthesizedAnnotationCacheKey(AnnotatedElement element, Class<? extends Annotation> targetAnnotation) {
            this.element = element;
            this.targetAnnotation = targetAnnotation;
        }

        private static SynthesizedAnnotationCacheKey of(AnnotatedElement element, Class<? extends Annotation> target) {
            return new SynthesizedAnnotationCacheKey(element, target);
        }

        @Override
        public boolean equals(Object other) {
            if (this == other) {
                return true;
            }
            if (!(other instanceof SynthesizedAnnotationCacheKey)) {
                return false;
            }
            SynthesizedAnnotationCacheKey otherKey = (SynthesizedAnnotationCacheKey) other;
            return (this.element.equals(otherKey.element) && this.targetAnnotation.equals(otherKey.targetAnnotation));
        }

        @Override
        public int hashCode() {
            return (this.element.hashCode() * 29 + this.targetAnnotation.hashCode());
        }
    }

}
