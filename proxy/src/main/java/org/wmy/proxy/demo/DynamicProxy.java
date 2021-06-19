package org.wmy.proxy.demo;

import org.wmy.proxy.bean.RequestInfo;
import org.wmy.proxy.util.MetricsCollector;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 * @author wmy
 * @create 2021-06-18 17:25
 */
public class DynamicProxy {

    private MetricsCollector metricsCollector;

    public DynamicProxy(MetricsCollector metricsCollector) {
        this.metricsCollector = metricsCollector;
    }

    /**
     * 根据动态代理产生代理对象
     * @param proxiedObject 被代理对象
     * @return 代理对象
     */
    public Object createProxy(Object proxiedObject) {

        Class<?>[] interfaces = proxiedObject.getClass().getInterfaces();
        DynamicProxyHandler dynamicProxyHandler = new DynamicProxyHandler(proxiedObject);
        // 创建动态代理对象
        return Proxy.newProxyInstance(this.getClass().getClassLoader(),
                interfaces,dynamicProxyHandler);
    }
    private class DynamicProxyHandler implements InvocationHandler {
        private Object proxiedObject;

        public DynamicProxyHandler(Object proxiedObject) {
            this.proxiedObject = proxiedObject;
        }

        /**
         *
         * @param proxy the proxy instance that the method was invoked on （代理对象）
         * @param method 方法
         * @param args 方法参数列表
         * @return 方法执行结果
         * @throws Throwable 异常
         */
        @Override
        public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
            long startTimestamp = System.currentTimeMillis();
            Object result = method.invoke(proxiedObject, args);
            long endTimestamp = System.currentTimeMillis();
            long responseTime = endTimestamp-startTimestamp;
            String apiName = proxiedObject.getClass().getName()+":"+method.getName();
            RequestInfo requestInfo = new RequestInfo(startTimestamp, responseTime, apiName);
            metricsCollector.recordRequestInfo(requestInfo);
            return result;
        }
    }
}
