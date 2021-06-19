package testBean;

import lombok.Data;

import java.io.Serializable;

/**
 * @author wmy
 * @create 2021-06-17 10:30
 */

@Data
public class Dog implements Serializable {
    private String name;
    private int age;

    public Dog(String name, int age) {
        this.name = name;
        this.age = age;
    }

    public Dog() {
    }
}
