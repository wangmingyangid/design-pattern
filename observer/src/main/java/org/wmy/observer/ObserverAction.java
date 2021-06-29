package org.wmy.observer;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * @author wmy
 * @date 2021-06-26 9:08
 */
public class ObserverAction {

    private Object object;
    private Method method;

    public ObserverAction(Object object, Method method) {
        this.object = object;
        this.method = method;
        method.setAccessible(true);
    }

    /**
     * 反射执行方法
     * @param event 消息类型
     */
    public void execute(Object event){
        try {
            method.invoke(object,event);
        } catch (IllegalAccessException  | InvocationTargetException e) {
            e.printStackTrace();
        }
    }

}
