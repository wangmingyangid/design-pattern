package org.wmy.proxy.aop.util;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.*;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import javax.jws.Oneway;
import java.util.Arrays;

/**
 * @author wmy
 * @create 2021-06-19 15:17
 *
 * 告诉 Spring 每个方法什么时候开始执行
 *
 *      try{
 *          执行 @Before
 *          method.invoke(obj,args);
 *          执行 @AfterReturning
 *      }catch(){
 *          执行 @AfterThrowing
 *      }finally{
 *          执行 @After
 *      }
 *
 *      5 个通知注解
 *      *  @Before： 在目标方法之前执行                    前置通知
 *      *  @AfterReturning：在目标方法正常返回之后         返回通知
 *      *  @AfterThrowing：在目标方法抛出异常之后          异常通知
 *      *  @After：在目标方法之后执行                     后置通知
 *      *  @Around：在目标方法之后执行                    环绕通知
 *
 */

@Component
@Aspect
@Order(2)
public class LogUtil {

    //切入点表达式：execution(访问权限符 返回值类型 方法完整签名)

    @Pointcut("execution(public int org.wmy.proxy.aop.service.MyCalculator.*(..))")
    private void MyPointcut(){}

    @Before("MyPointcut()")
    private void logStart(JoinPoint joinPoint){
        Object[] args = joinPoint.getArgs();
        Signature signature = joinPoint.getSignature();
        String name = signature.getName();
        System.out.println("LogUtil:"+name+"方法开始执行，用的参数列表"+Arrays.asList(args));
    }

    // returning ="result" 告诉spring result 这个参数用来接收返回值
    // Object result 指定通知方法可以接收什么类型的返回值，尽量写大，否则通知方法不会执行
    @AfterReturning(value = "MyPointcut()", returning ="result")
    private void logReturn(JoinPoint joinPoint,Object result){
        Signature signature = joinPoint.getSignature();
        String name = signature.getName();
        System.out.println("LogUtil:"+name+"方法正常执行完成，返回值是："+result);
    }

    // throwing = "exception" 告诉spring exception 这个参数用来接收异常
    // Exception exception 指定通知方法可以接收什么类型的异常，尽量写大，否则通知方法不会执行
    @AfterThrowing(value = "MyPointcut()", throwing = "exception")
    private void logException(JoinPoint joinPoint,Exception exception){
        Signature signature = joinPoint.getSignature();
        String name = signature.getName();
        System.out.println("LogUtil:"+name+"方法执行出现异常，异常信息是："+exception);
    }

    @After("MyPointcut()")
    private void logEnd(JoinPoint joinPoint){
        Signature signature = joinPoint.getSignature();
        String name = signature.getName();
        System.out.println("LogUtil:"+name+"方法结束");
    }

    /**
     * 4合一通知就是环绕通知，就是动态代理
     */
    @Around("MyPointcut()")
    private Object myAround(ProceedingJoinPoint pjp) throws Throwable {
        Object[] args = pjp.getArgs();
        // TODO 在这里可以篡改方法的参数，改变入参
        String name = pjp.getSignature().getName();
        Object result = null;
        try {
            System.out.println("LogUtil:"+"环绕通知"+name+"方法开始执行，用的参数列表"+Arrays.asList(args));
            // 该方法就是利用反射调用目标方法，相当于 method.invoke(obj,args)
            result =  pjp.proceed();
            System.out.println("LogUtil:"+"环绕通知"+name+"方法正常执行完成，返回值是："+result);
        }catch (Exception e){
            System.out.println("LogUtil:"+"环绕通知"+name+"方法执行出现异常，异常信息是："+e);
        }finally {
            System.out.println("LogUtil:"+"环绕通知"+name+"方法结束");
        }
        // 该返回值，就是被代理的对象方法执行完后的结果，一定要返回出去
        // TODO 在这里可以篡改返回值
        return result;
    }
}
