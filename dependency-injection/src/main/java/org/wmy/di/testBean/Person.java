package org.wmy.di.testBean;

import lombok.Data;
import lombok.ToString;

/**
 * @author wmy
 * @create 2021-06-17 10:29
 */

@Data
@ToString
public class Person {
    private String name;
    private int age;
    private Dog dog;

    public Person(Dog dog) {
        this.dog = dog;
    }
}
