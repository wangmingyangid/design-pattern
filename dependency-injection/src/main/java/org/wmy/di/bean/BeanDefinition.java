package org.wmy.di.bean;

import lombok.Data;

import javax.xml.bind.annotation.*;
import java.util.List;

/**
 * @author wmy
 * @create 2021-06-15 14:46
 */

@Data
@XmlType
@XmlAccessorType(XmlAccessType.FIELD)
public class BeanDefinition {
    @XmlAttribute(name = "id")
    private String id;
    @XmlAttribute(name = "class")
    private String className;
    @XmlElement(name = "constructor-arg")
    private List<ConstructorArg> args;
    // 是单例否
    @XmlAttribute(name = "scope")
    private String scope;
    // 是否懒加载
    @XmlAttribute(name = "lazy-init")
    private boolean lazyInit;

    public boolean isSingleton(){
        return "singleton".equals(scope);
    }

}
