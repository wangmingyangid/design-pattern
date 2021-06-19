package org.wmy.di.bean;

import lombok.Data;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;

/**
 * @author wmy
 * @create 2021-06-15 15:19
 */

@Data
@XmlType
@XmlAccessorType(XmlAccessType.FIELD)
public class ConstructorArg {

    // 是引用类型，填写引用的id
    @XmlAttribute(name = "ref")
    private String ref;
    @XmlAttribute(name = "type")
    private String type;
    @XmlAttribute(name = "value")
    private String value;

    public boolean isRef(){
        if (ref!=null){
            return true;
        }
        return false;
    }

    public Class acquireType()  {

        if ("int".equalsIgnoreCase(type)) {
            return int.class;
        }
        if ("string".equalsIgnoreCase(type)) {
            return String.class;
        }
        if ("boolean".equalsIgnoreCase(type)) {
            return boolean.class;
        }
        return null;
    }



    public Object acquireValue(){

        if ("int".equalsIgnoreCase(type)){
            return Integer.valueOf(value);
        }

        if ("string".equalsIgnoreCase(type)) {
            return value;
        }
        throw new RuntimeException("不支持的类型");
    }

}
