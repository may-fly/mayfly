package mayfly.core.util;

import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.List;

/**
 * @author meilin.huang
 * @version 1.0
 * @date 2020-01-15 10:10 上午
 */
public class ResourceUtils {
    /**
     * 类路径下获取资源
     *
     * @param fileName 文件名或者文件路径（classpath为根路径）
     * @return 资源url
     */
    public static List<URL> getResources(String fileName) {
        Assert.notNull(fileName, "文件名不能为空");
        try {
            // 根据文件名加载所有的同名文件
            Enumeration<URL> urls = ClassUtils.getClassLoader(ResourceUtils.class).getResources(fileName);
            if (urls != null) {
                List<URL> us = new ArrayList<>();
                while (urls.hasMoreElements()) {
                    us.add(urls.nextElement());
                }
                return us;
            }
            return Collections.emptyList();
        } catch (Throwable t) {
            throw new IllegalArgumentException("获取文件资源失败", t);
        }
    }
}
