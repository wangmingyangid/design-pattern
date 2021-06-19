package org.wmy.di.parser;

import org.wmy.di.bean.BeanDefinition;

import java.io.InputStream;
import java.util.List;

/**
 * @author wmy
 * @create 2021-06-15 14:44
 */
public interface BeanConfigParser {
    List<BeanDefinition> parse(InputStream inputStream);
    List<BeanDefinition> parse(String configContent);
}
