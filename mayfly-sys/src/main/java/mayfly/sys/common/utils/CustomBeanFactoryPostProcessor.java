package mayfly.sys.common.utils;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.stereotype.Component;

import java.lang.management.ManagementFactory;

/**
 * @author meilin.huang
 * @version 1.0
 * @date 2019-12-25 5:36 下午
 */
@Component
public class CustomBeanFactoryPostProcessor implements BeanFactoryPostProcessor {
    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {
        ManagementFactory.getClassLoadingMXBean();
//        System.out.println(beanFactory.getBeanDefinition("test"));
    }
}
