Spring Aop 学习

0 基本概念



1 环境搭建

1. 引入依赖

    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-aop</artifactId>
    </dependency>

2. 编写需要加入切面的方法

    @Service
    public class MyCalculator {
    
        public int add(int i,int j){
            int result = i+j;
            return result;
        }
        public int sub(int i,int j){
            int result = i-j;
            return result;
        }
        public int mul(int i,int j){
            int result = i*j;
            return result;
        }
        public int dev(int i,int j){
            int result = i/j;
            return result;
        }
    }

3. 准备切面类

    public class LogUtil {
        
        private void logStart(){
            System.out.println("【xxx】方法开始执行，用的参数列表【xxx】");
        }
        private void logReturn(){
            System.out.println("【xxx】方法开始正常执行完成，用的参数列表【xxx】");
        }
        private void logException(){
            System.out.println("【xxx】方法执行出现异常，用的参数列表【xxx】");
        }
        private void logEnd(){
            System.out.println("【xxx】方法结束，用的参数列表【xxx】");
        }
    }

2 aop 知识

2.1 注解

    5 个通知注解
    @Before： 在目标方法之前执行                   前置通知
    @AfterReturning：在目标方法正常返回之后         返回通知
    @AfterThrowing：在目标方法抛出异常之后          异常通知
    @After：在目标方法之后执行                     后置通知
    @Around：在目标方法之后执行                    环绕通知

这几个注解的意思是告诉 Spring 切面类的每个方法什么时候开始执行

    try{
        @Before
        method.invoke(obj,args);// 目标方法
        @AfterReturning
    }catch(){
        @AfterThrowing
    }finally{
        @After
    }

2.2 切入点表达式 

注解并没有告诉在哪个类的哪个方法上执行，所以需要用到切入点表达式

    @Component
    @Aspect
    public class LogUtil {
    
        //切入点表达式：execution(访问权限符 返回值类型 方法完整签名)
    
        @Before("execution(public int org.wmy.proxy.aop.service.MyCalculator.*(int,int))")
        private void logStart(){
            System.out.println("【xxx】方法开始执行，用的参数列表【xxx】");
        }
        @AfterReturning("execution(public int org.wmy.proxy.aop.service.MyCalculator.*(int,int))")
        private void logReturn(){
            System.out.println("【xxx】方法开始正常执行完成，用的参数列表【xxx】");
        }
    
        @AfterThrowing("execution(public int org.wmy.proxy.aop.service.MyCalculator.*(int,int))")
        private void logException(){
            System.out.println("【xxx】方法执行出现异常，用的参数列表【xxx】");
        }
    
        @After(("execution(public int org.wmy.proxy.aop.service.MyCalculator.*(int,int))"))
        private void logEnd(){
            System.out.println("【xxx】方法结束，用的参数列表【xxx】");
        }
    }
    

    切入点表达式格式：execution(访问权限符 返回值类型 方法全类名(参数表))
    通配符：
    *：
    	1）匹配一个或者多个字符
    		匹配的类：开头My，结尾r，匹配该类的任何方法
    		execution(public int org.wmy.proxy.aop.service.My*r.*(int,int))
    	2）匹配任意一个参数
    		第一个参数式 int 类型，第二个参数式任意类型（匹配两个参数）
    		execution(public int org.wmy.proxy.aop.service.MyCalculator.*(int,*))
    	3)只能匹配一层路径
    		execution(public int org.wmy.proxy.aop.*.MyCalculator.*(int,int))
    	4)权限位置不能写
    		错误；不写就代表任意权限
    		execution(* int org.wmy.proxy.aop.service.MyCalculator.*(int,int))
    ..:
    	1)匹配任意多个参数，任意参数类型
    		execution(public int org.wmy.proxy.aop.service.MyCalculator.*(..))
    	2)匹配任意多层路径
    		execution(public int org.wmy.proxy..MyCalculator.*(int,int))

2.3 JoinPoint

    可以在通知方法运行的时候，拿到目标方法的详细信息
    只需要在通知方法的参数列表上写一个参数：JoinPoint
    该类封装了当前目标方法的详细信息

    @Before("execution(public int org.wmy.proxy.aop.service.MyCalculator.*(int,int))")
    private void logStart(JoinPoint joinPoint){
        Object[] args = joinPoint.getArgs();
        Signature signature = joinPoint.getSignature();
        String name = signature.getName();
        System.out.println(name+"方法开始执行，用的参数列表"+Arrays.asList(args));
    }
    // returning ="result" 告诉spring result 这个参数用来接收返回值
    // Object result 指定通知方法可以接收什么类型的返回值，尽量写大，否则通知方法不会执行
    @AfterReturning(value = "execution(public int org.wmy.proxy.aop.service.MyCalculator.*(int,int))",
                    returning ="result")
    private void logReturn(JoinPoint joinPoint,Object result){
        Signature signature = joinPoint.getSignature();
        String name = signature.getName();
        System.out.println(name+"方法正常执行完成，返回值是："+result);
    }
    
    // throwing = "exception" 告诉spring exception 这个参数用来接收异常
    // Exception exception 指定通知方法可以接收什么类型的异常，尽量写大，否则通知方法不会执行
    @AfterThrowing(value = "execution(public int org.wmy.proxy.aop.service.MyCalculator.*(int,int))",
                   throwing = "exception")
    private void logException(JoinPoint joinPoint,Exception exception){
        Signature signature = joinPoint.getSignature();
        String name = signature.getName();
        System.out.println(name+"方法执行出现异常，异常信息是："+exception);
    }

2.4 spring 对通知方法的要求

Spring 对通知方法的要求不严格；唯一要求就是参数列表不能乱写。

通知方法是Spring 利用反射进行调用的，每次方法调用要确定这个方法的参数表的值。

参数列表上的每一个参数，Spring 都必须知道是什么

JoinPoint ：spring 是认识的

不知道的参数一定要告诉Spring 这是什么，比如用throwing、returning

2.5 抽取可重用的切入点表达式

1. 随便声明一个没有实现的返回void的空方法
2. 给方法上标注@Pointcut 注解

    @Component
    @Aspect
    public class LogUtil {
    
        @Pointcut("execution(public int org.wmy.proxy.aop.service.MyCalculator.*(..))")
        private void MyPointcut(){}
    
        @Before("MyPointcut()")
        private void logStart(JoinPoint joinPoint){
            Object[] args = joinPoint.getArgs();
            Signature signature = joinPoint.getSignature();
            String name = signature.getName();
            System.out.println(name+"方法开始执行，用的参数列表"+Arrays.asList(args));
        }
    
        // returning ="result" 告诉spring result 这个参数用来接收返回值
        // Object result 指定通知方法可以接收什么类型的返回值，尽量写大，否则通知方法不会执行
        @AfterReturning(value = "MyPointcut()", returning ="result")
        private void logReturn(JoinPoint joinPoint,Object result){
            Signature signature = joinPoint.getSignature();
            String name = signature.getName();
            System.out.println(name+"方法正常执行完成，返回值是："+result);
        }
    
        // throwing = "exception" 告诉spring exception 这个参数用来接收异常
        // Exception exception 指定通知方法可以接收什么类型的异常，尽量写大，否则通知方法不会执行
        @AfterThrowing(value = "MyPointcut()", throwing = "exception")
        private void logException(JoinPoint joinPoint,Exception exception){
            Signature signature = joinPoint.getSignature();
            String name = signature.getName();
            System.out.println(name+"方法执行出现异常，异常信息是："+exception);
        }
    
        @After("MyPointcut()")
        private void logEnd(JoinPoint joinPoint){
            Signature signature = joinPoint.getSignature();
            String name = signature.getName();
            System.out.println(name+"方法结束");
        }
    }

2.6 环绕通知

如果想影响目标方法的执行，可以使用环绕通知；（可修改入参和修改返回值）

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
                System.out.println("环绕通知"+name+"方法开始执行，用的参数列表"+Arrays.asList(args));
                // 该方法就是利用反射调用目标方法，相当于 method.invoke(obj,args)
               result =  pjp.proceed();
                System.out.println("环绕通知"+name+"方法正常执行完成，返回值是："+result);
            }catch (Exception e){
                System.out.println("环绕通知"+name+"方法执行出现异常，异常信息是："+e);
            }finally {
                System.out.println("环绕通知"+name+"方法结束");
            }
            // 该返回值，就是被代理的对象方法执行完后的结果，一定要返回出去
            // TODO 在这里可以篡改返回值
            return result;
        }

当环绕通知和普通通知同时开启时，执行顺序：

    @Component
    @Aspect
    public class LogUtil {
    
        //切入点表达式：execution(访问权限符 返回值类型 方法完整签名)
    
        @Pointcut("execution(public int org.wmy.proxy.aop.service.MyCalculator.*(..))")
        private void MyPointcut(){}
    
        @Before("MyPointcut()")
        private void logStart(JoinPoint joinPoint){
            Object[] args = joinPoint.getArgs();
            Signature signature = joinPoint.getSignature();
            String name = signature.getName();
            System.out.println(name+"方法开始执行，用的参数列表"+Arrays.asList(args));
        }
    
        // returning ="result" 告诉spring result 这个参数用来接收返回值
        // Object result 指定通知方法可以接收什么类型的返回值，尽量写大，否则通知方法不会执行
        @AfterReturning(value = "MyPointcut()", returning ="result")
        private void logReturn(JoinPoint joinPoint,Object result){
            Signature signature = joinPoint.getSignature();
            String name = signature.getName();
            System.out.println(name+"方法正常执行完成，返回值是："+result);
        }
    
        // throwing = "exception" 告诉spring exception 这个参数用来接收异常
        // Exception exception 指定通知方法可以接收什么类型的异常，尽量写大，否则通知方法不会执行
        @AfterThrowing(value = "MyPointcut()", throwing = "exception")
        private void logException(JoinPoint joinPoint,Exception exception){
            Signature signature = joinPoint.getSignature();
            String name = signature.getName();
            System.out.println(name+"方法执行出现异常，异常信息是："+exception);
        }
    
        @After("MyPointcut()")
        private void logEnd(JoinPoint joinPoint){
            Signature signature = joinPoint.getSignature();
            String name = signature.getName();
            System.out.println(name+"方法结束");
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
                System.out.println("环绕通知"+name+"方法开始执行，用的参数列表"+Arrays.asList(args));
                // 该方法就是利用反射调用目标方法，相当于 method.invoke(obj,args)
               result =  pjp.proceed();
                System.out.println("环绕通知"+name+"方法正常执行完成，返回值是："+result);
            }catch (Exception e){
                System.out.println("环绕通知"+name+"方法执行出现异常，异常信息是："+e);
            }finally {
                System.out.println("环绕通知"+name+"方法结束");
            }
            // 该返回值，就是被代理的对象方法执行完后的结果，一定要返回出去
            // TODO 在这里可以篡改返回值
            return result;
        }
    }
    
    =======================================================================
    @Test
    public void test01(){
        // class org.wmy.proxy.aop.service.MyCalculator$$EnhancerBySpringCGLIB$$11f09e81
        System.out.println(myCalculator.getClass());
        myCalculator.add(1,2);
    }

    class org.wmy.proxy.aop.service.MyCalculator$$EnhancerBySpringCGLIB$$51fa6b35
    环绕通知add方法开始执行，用的参数列表[1, 2]
    add方法开始执行，用的参数列表[1, 2]
    方法内部执行
    add方法正常执行完成，返回值是：3
    add方法结束
    环绕通知add方法正常执行完成，返回值是：3
    环绕通知add方法结束

环绕前置---普通前置---目标方法执行---普通返回或异常---普通后置---环绕返回或异常---环绕后置

2.7 多个切面的执行顺序

2.7.1 不含环绕通知时两个切面的执行顺序

增加一个切面类：ValidateUtil，用于有效值判断

    @Component
    @Aspect
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
    

    // 不含环绕通知时两个切面的执行顺序
    
    LogUtil:add方法开始执行，用的参数列表[1, 2]
    ValidateUtil:add方法开始执行，用的参数列表[1, 2]
    方法内部执行
    ValidateUtil:add方法正常执行完成，返回值是：3
    ValidateUtil:add方法结束
    LogUtil:add方法正常执行完成，返回值是：3
    LogUtil:add方法结束



为什么是先进LogUtil呢？

1. 根据切面类的首字母排序，越小，优先级越高
2. 可以使用@Order 注解决定哪个切面优先执行

    @Component
    @Aspect
    @Order(1)
    public class ValidateUtil {}
    
    @Component
    @Aspect
    @Order(2)
    public class LogUtil {}
    
    ============================================
    ValidateUtil:add方法开始执行，用的参数列表[1, 2]
    LogUtil:add方法开始执行，用的参数列表[1, 2]
    方法内部执行
    LogUtil:add方法正常执行完成，返回值是：3
    LogUtil:add方法结束
    ValidateUtil:add方法正常执行完成，返回值是：3
    ValidateUtil:add方法结束

2.7.2 含环绕通知时两个切面的执行顺序

    @Component
    @Aspect
    @Order(1)
    public class LogUtil {}
    
    ======================
    @Component
    @Aspect
    @Order(2)
    public class ValidateUtil {}

    LogUtil:环绕通知add方法开始执行，用的参数列表[1, 2]
    LogUtil:add方法开始执行，用的参数列表[1, 2]
    ValidateUtil:add方法开始执行，用的参数列表[1, 2]
    方法内部执行
    ValidateUtil:add方法正常执行完成，返回值是：3
    ValidateUtil:add方法结束
    LogUtil:add方法正常执行完成，返回值是：3
    LogUtil:add方法结束
    LogUtil:环绕通知add方法正常执行完成，返回值是：3
    LogUtil:环绕通知add方法结束

改变切面类的优先级后：

    @Component
    @Aspect
    @Order(1)
    public class ValidateUtil {}
    ===============================
    @Component
    @Aspect
    @Order(2)
    public class LogUtil {}

    ValidateUtil:add方法开始执行，用的参数列表[1, 2]
    LogUtil:环绕通知add方法开始执行，用的参数列表[1, 2]
    LogUtil:add方法开始执行，用的参数列表[1, 2]
    方法内部执行
    LogUtil:add方法正常执行完成，返回值是：3
    LogUtil:add方法结束
    LogUtil:环绕通知add方法正常执行完成，返回值是：3
    LogUtil:环绕通知add方法结束
    ValidateUtil:add方法正常执行完成，返回值是：3
    ValidateUtil:add方法结束

2.8 总结

不管是一个切面还是多个切面，遵循的规律都是，先进后出。

同一个切面中：

环绕通知优先级高于普通通知，执行顺序是：

环绕前置---普通前置---目标方法执行---普通返回或异常---普通后置---环绕返回或异常---环绕后置

多个切面中：

1. 字母顺序或@Order注解决定了，哪个切面先执行；
2. 先执行的切面中，如果有环绕通知，环绕通知优先执行；
3. 遵循先进后出的规律。


























