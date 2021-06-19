package org.wmy.di.parser;

import org.wmy.di.bean.BeanDefinition;
import org.wmy.di.bean.RootBean;

import javax.xml.bind.JAXB;
import java.io.InputStream;
import java.util.List;

/**
 * @author wmy
 * @create 2021-06-15 14:47
 */
public class XmlBeanConfigParser implements BeanConfigParser {

    @Override
    public List<BeanDefinition> parse(InputStream inputStream) {
        RootBean rootBean = JAXB.unmarshal(inputStream, RootBean.class);
        List<BeanDefinition> beanDefinitions = rootBean.getBeanDefinitions();

        return beanDefinitions;
    }

    @Override
    public List<BeanDefinition> parse(String configContent) {
        return null;
    }
}
