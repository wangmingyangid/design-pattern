package org.wmy.di.factory;


import org.wmy.di.bean.BeanDefinition;
import org.wmy.di.bean.ConstructorArg;

import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author wmy
 * @create 2021-06-15 14:42
 */
public class BeansFactory {

    // BeanDefinition 的缓存  String -- 代表id
    private ConcurrentHashMap<String,BeanDefinition> beanDefinitions = new ConcurrentHashMap<>();
    // 单例对象的缓存 String -- 代表id
    private ConcurrentHashMap<String,Object> singletonObjects = new ConcurrentHashMap<>();


    public void addBeanDefinitions(List<BeanDefinition> definitions){
        // 把definitions缓存起来 (因为不是所有的对象都需要立刻创建)
        definitions.forEach(e->beanDefinitions.put(e.getId(),e));
        // 对于非懒加载且是单例的对象，立即创建
        definitions.forEach(e->{
            if (!e.isLazyInit()&&e.isSingleton()){
                createBean(e);
            }
        });

    }


    /**
     *  根据定义的 util 信息创建对象；如果要创建的对象依赖其它对象，则递归创建依赖的对象
     * @param definition util 的信息
     * @return
     */
    private Object createBean(BeanDefinition definition) {
        // 单例且存在缓存中，直接返回
        if (definition.isSingleton() && singletonObjects.contains(definition.getId())){
            return singletonObjects.get(definition.getId());
        }
        // 根据反射创建对象
        Object bean = null;
        try {
            // TODO name的表示：必须是全限定名
            Class<?> beanClass = Class.forName(definition.getClassName());
            List<ConstructorArg> args = definition.getArgs();
            // 构造列表为空
            if (args.isEmpty()){
                bean = beanClass.newInstance();
            }else {
                // 构造列表不为空
                // 构造类型数组和参数值数组
                Class[] argClasses = new Class[args.size()];
                Object[] argObjects = new Object[args.size()];
                for (int i = 0;i<args.size();i++) {
                    ConstructorArg constructorArg = args.get(i);
                    // 非引用参数
                    if (!constructorArg.isRef()){
                        // TODO 基本对象类型，如何创建 Class? 直接 int.class  String.class  boolean.class
                        argClasses[i] = constructorArg.acquireType();
                        argObjects[i] = constructorArg.acquireValue();
                    }else {
                        // 引用型参数，先创造该对象
                        BeanDefinition refBeanDefinition = beanDefinitions.get(constructorArg.getRef());
                        if(refBeanDefinition == null){
                            throw new RuntimeException("Bean is not defined: " + constructorArg.getRef());
                        }
                        argClasses[i] = Class.forName(refBeanDefinition.getClassName());
                        // 递归创建
                        argObjects[i] = createBean(refBeanDefinition);
                    }
                }
                // 有构造函数时创建对象
                bean = beanClass.getConstructor(argClasses).newInstance(argObjects);
            }

        } catch (ClassNotFoundException | IllegalAccessException
                | InstantiationException | NoSuchMethodException
                | InvocationTargetException e) {
            e.printStackTrace();
        }

        // 是否加入单例缓存
        if (bean!=null && definition.isSingleton()){
            singletonObjects.putIfAbsent(definition.getId(),bean);
        }

        return bean;
    }

    public Object getBean(String beanId){
        BeanDefinition beanDefinition = beanDefinitions.get(beanId);
        if (beanDefinition == null) {
            throw new RuntimeException("Bean is not defined: " + beanId);
        }
        return createBean(beanDefinition);
    }
}
