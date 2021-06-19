package org.wmy.di.context;

import org.wmy.di.bean.BeanDefinition;
import org.wmy.di.factory.BeansFactory;
import org.wmy.di.parser.BeanConfigParser;
import org.wmy.di.parser.XmlBeanConfigParser;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

/**
 * @author wmy
 * @create 2021-06-15 14:39
 */
public class PathXmlApplicationContext implements ApplicationContext {

    // 创建beans
    private BeansFactory beansFactory;
    // 解析器
    private BeanConfigParser parser;

    public PathXmlApplicationContext(String configLocation) {
        beansFactory = new BeansFactory();
        parser = new XmlBeanConfigParser();
        loadBeanDefinitions(configLocation);
    }

    private void loadBeanDefinitions(String configLocation) {
        // 读取配置文件
        InputStream inputStream = null;
        try {
            // path 不以’/'开头时默认是从此类所在的包下取资源，以’/'开头则是从 ClassPath 根下获取。
            inputStream = this.getClass().getResourceAsStream("/" + configLocation);
            if (inputStream == null){
                throw new RuntimeException("Can not find config file: "+configLocation);
            }

            // 解析器进行解析，得到 Bean 的信息集合
            List<BeanDefinition> definitions = parser.parse(inputStream);
            // 把得到的 BeanDefinition 放入工厂
            beansFactory.addBeanDefinitions(definitions);

        }finally {
            if (inputStream != null){
                try {
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @Override
    public Object getBean(String beanId) {
        return beansFactory.getBean(beanId);
    }
}
