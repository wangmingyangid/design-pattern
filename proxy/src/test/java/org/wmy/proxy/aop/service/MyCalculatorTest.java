package org.wmy.proxy.aop.service;


import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;

/**
 * @author wmy
 * @create 2021-06-19 16:15
 */

@RunWith(SpringRunner.class)
@SpringBootTest
public class MyCalculatorTest {

    @Resource
    private MyCalculator myCalculator;

    /**
     * spring aop 的被代理对象必须实现接口，否则无法创建代理对象
     * 但是 spring-boot-starter-aop 这个启动器，包含了 AspectJ，可以
     * 给没有接口的类创建代理对象
     */
    @Test
    public void test01(){
        // class org.wmy.proxy.aop.service.MyCalculator$$EnhancerBySpringCGLIB$$11f09e81
        System.out.println(myCalculator.getClass());
        myCalculator.add(1,2);
    }

    /**
     * 通知方法的执行顺序
     * 正常执行： @Before ==> @AfterReturning ==> @After
     * 异常执行： @Before ==> @AfterThrowing ==> @After
     * 环绕通知和普通通知同时作用：
     *      环绕前置---普通前置---目标方法执行---普通返回或异常---普通后置---环绕正常或异常---环绕后置
     */
    @Test
    public void test02(){
        myCalculator.div(1,0);
    }

    /**
     * 测试 JoinPoint
     */
    @Test
    public void test03(){
        myCalculator.add(1,2);
        System.out.println("=======================");
        myCalculator.div(1,0);
    }
}
