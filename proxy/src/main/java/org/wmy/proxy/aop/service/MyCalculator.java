package org.wmy.proxy.aop.service;

import org.springframework.stereotype.Service;

/**
 * @author wmy
 * @create 2021-06-19 15:14
 */
@Service
public class MyCalculator {

    public int add(int i,int j){
        int result = i+j;
        System.out.println("方法内部执行");
        return result;
    }
    public int sub(int i,int j){
        int result = i-j;
        System.out.println("方法内部执行");
        return result;
    }
    public int mul(int i,int j){
        int result = i*j;
        System.out.println("方法内部执行");
        return result;
    }
    public int div(int i,int j){
        int result = i/j;
        System.out.println("方法内部执行");
        return result;
    }
}
