import cn.hutool.core.bean.BeanUtil;
import testBean.Dog;
import testBean.Person;


/**
 * @author wmy
 * @create 2021-06-17 16:49
 */
public class TestForHuTool {

    public static void main(String[] args) {
        Dog dog = new Dog();
        dog.setAge(10);
        dog.setName("旺财");
        Person person = new Person(dog);
        person.setAge(15);
        person.setName("wmy");

        // 经过测试可知，实现的是浅拷贝
        Person person1 = BeanUtil.copyProperties(person, Person.class);
        System.out.println(person1);
    }
}
