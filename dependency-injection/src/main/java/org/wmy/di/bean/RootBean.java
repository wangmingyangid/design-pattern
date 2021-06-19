package org.wmy.di.bean;

import lombok.Data;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

/**
 * @author wmy
 * @create 2021-06-16 18:22
 */

@Data
@XmlRootElement(name = "beans")
@XmlAccessorType(XmlAccessType.FIELD)
public class RootBean {

    @XmlElement(name = "util")
    private List<BeanDefinition> beanDefinitions;

}
