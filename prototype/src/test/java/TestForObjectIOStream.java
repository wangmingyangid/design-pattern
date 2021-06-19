import testBean.Dog;
import testBean.Person;

import java.io.*;

/**
 * @author wmy
 * @create 2021-06-17 19:10
 */
public class TestForObjectIOStream {

    public static void main(String[] args) {
        Dog dog = new Dog();
        dog.setAge(10);
        dog.setName("旺财");
        Person person = new Person(dog);
        person.setAge(15);
        person.setName("wmy");

        Object deepCopy = deepCopy(person);
        System.out.println(deepCopy);
    }

    private static Object deepCopy(Object object) {
        ByteArrayOutputStream bo;
        ObjectOutputStream oo = null;
        ByteArrayInputStream bi;
        ObjectInputStream oi = null;
        try{
            bo = new ByteArrayOutputStream();
            oo = new ObjectOutputStream(bo);
            // 获取内存中的缓存数据并转成数组
            oo.writeObject(object);

            // bo.toByteArray() 看源码会进行数组复制
            bi = new ByteArrayInputStream(bo.toByteArray());
            oi = new ObjectInputStream(bi);
            return oi.readObject();

        }catch (IOException | ClassNotFoundException e){
            e.printStackTrace();
            return null;
        }finally {
            try {
                oo.close();
                oi.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
