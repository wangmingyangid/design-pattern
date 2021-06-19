package org.wmy.di.test;

import org.wmy.di.context.ApplicationContext;
import org.wmy.di.context.PathXmlApplicationContext;
import org.wmy.di.testBean.Person;

/**
 * @author wmy
 * @create 2021-06-16 17:41
 */
public class Test {
    public static void main(String[] args) {

        ApplicationContext ioc = new PathXmlApplicationContext("beans.xml");
        Person person = (Person) ioc.getBean("person");
        System.out.println(person.toString());
    }
}
