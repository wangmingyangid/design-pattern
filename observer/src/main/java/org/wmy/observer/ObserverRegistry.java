package org.wmy.observer;

import java.lang.reflect.Method;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArraySet;

/**
 * @author wmy
 * @date 2021-06-26 9:12
 *
 * 注册表类
 */
public class ObserverRegistry {

    private ConcurrentHashMap<Class<?>,CopyOnWriteArraySet<ObserverAction>> registry = new ConcurrentHashMap<>();

    /**
     * 给注册表中注册对象，把标注有@Subscribe 注解的方法找出来
     * @param observer
     */
    public void register(Object observer){
        // 根据对象获取类，找到所有声明方法，看是否标注有 @Subscribe
        Class<?> clazz = observer.getClass();
        Method[] declaredMethods = clazz.getDeclaredMethods();
        ArrayList<Method> methods = new ArrayList<>();
        for (Method method : declaredMethods) {
            if (method.isAnnotationPresent(Subscribe.class)){
                Class<?>[] parameterTypes = method.getParameterTypes();
                // 方法的参数只有一个
                if (parameterTypes.length == 1) {
                    methods.add(method);
                }
            }
        }
        Map<Class<?>,Collection<ObserverAction>> observerActions  = new HashMap<>();
        // 构建ObserverAction
        for (Method method : methods) {
            Class<?>[] parameterTypes = method.getParameterTypes();
            Class<?> eventType = parameterTypes[0];
            if (!observerActions.containsKey(eventType)){
                observerActions.put(eventType,new ArrayList<>());
            }
            observerActions.get(eventType).add(new ObserverAction(observer,method));
        }
        // 把从方法里构建出来的 observerActions 放入注册表中
        for (Map.Entry<Class<?>, Collection<ObserverAction>> entry : observerActions.entrySet()) {
            Class<?> eventType = entry.getKey();
            Collection<ObserverAction> actions = entry.getValue();
            CopyOnWriteArraySet<ObserverAction> registeredEventActions = registry.get(eventType);
            if (registeredEventActions == null){
                registry.putIfAbsent(eventType,new CopyOnWriteArraySet<>());
                registeredEventActions = registry.get(eventType);
            }
            registeredEventActions.addAll(actions);
        }
    }
    public List<ObserverAction> getMatchedObserverActions(Object event) {
        List<ObserverAction> matchedObservers = new ArrayList<>();
        Class<?> eventClass = event.getClass();
        for (Map.Entry<Class<?>, CopyOnWriteArraySet<ObserverAction>> entry : registry.entrySet()) {
            Class<?> type = entry.getKey();
            CopyOnWriteArraySet<ObserverAction> observerActions = entry.getValue();
            if (eventClass.isAssignableFrom(type)){
                matchedObservers.addAll(observerActions);
            }
        }
        return matchedObservers;
    }
}
