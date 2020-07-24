package mayfly.core.util;

import org.springframework.util.Assert;
import org.springframework.util.ClassUtils;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.Enumeration;
import java.util.Properties;

/**
 * @author meilin.huang
 * @date 2020-07-24 8:50 上午
 */
public class PropertiesUtils {

    /**
     * 加载properties文件
     *
     * @param resourceName 资源名
     * @param classLoader  class loader
     * @return properties
     * @throws IOException io异常
     */
    public static Properties load(String resourceName, ClassLoader classLoader) throws IOException {
        Assert.notNull(resourceName, "Resource name must not be null");
        ClassLoader classLoaderToUse = classLoader;
        if (classLoaderToUse == null) {
            classLoaderToUse = ClassUtils.getDefaultClassLoader();
        }
        Enumeration<URL> urls = (classLoaderToUse != null ? classLoaderToUse.getResources(resourceName) :
                ClassLoader.getSystemResources(resourceName));
        Properties props = new Properties();
        while (urls.hasMoreElements()) {
            URL url = urls.nextElement();
            URLConnection con = url.openConnection();
            try (InputStream is = con.getInputStream()) {
                if (resourceName.endsWith(FileUtils.XML_FILE_EXTENSION)) {
                    props.loadFromXML(is);
                } else {
                    props.load(is);
                }
            }
        }
        return props;
    }
}
