package org.wmy.proxy.aop.util;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.*;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.util.Arrays;

/**
 * @author wmy
 * @create 2021-06-19 20:57
 */

@Component
@Aspect
@Order(1)
public class ValidateUtil {
    //切入点表达式：execution(访问权限符 返回值类型 方法完整签名)

    @Pointcut("execution(public int org.wmy.proxy.aop.service.MyCalculator.*(..))")
    private void ValidatePointcut(){}

    @Before("ValidatePointcut()")
    private void validateStart(JoinPoint joinPoint){
        Object[] args = joinPoint.getArgs();
        Signature signature = joinPoint.getSignature();
        String name = signature.getName();
        System.out.println("ValidateUtil:"+name+"方法开始执行，用的参数列表"+Arrays.asList(args));
    }

    // returning ="result" 告诉spring result 这个参数用来接收返回值
    // Object result 指定通知方法可以接收什么类型的返回值，尽量写大，否则通知方法不会执行
    @AfterReturning(value = "ValidatePointcut()", returning ="result")
    private void validateReturn(JoinPoint joinPoint,Object result){
        Signature signature = joinPoint.getSignature();
        String name = signature.getName();
        System.out.println("ValidateUtil:"+name+"方法正常执行完成，返回值是："+result);
    }

    // throwing = "exception" 告诉spring exception 这个参数用来接收异常
    // Exception exception 指定通知方法可以接收什么类型的异常，尽量写大，否则通知方法不会执行
    @AfterThrowing(value = "ValidatePointcut()", throwing = "exception")
    private void validateException(JoinPoint joinPoint,Exception exception){
        Signature signature = joinPoint.getSignature();
        String name = signature.getName();
        System.out.println("ValidateUtil:"+name+"方法执行出现异常，异常信息是："+exception);
    }

    @After("ValidatePointcut()")
    private void validateEnd(JoinPoint joinPoint){
        Signature signature = joinPoint.getSignature();
        String name = signature.getName();
        System.out.println("ValidateUtil:"+name+"方法结束");
    }

}
