package reflection.zq.basicclass.demo;

import java.lang.reflect.*;

public class RunDemo {
    public static void main(String[] args){
//        String[] strs = {"Hello", "World", "I", "am", "zhaoq"};
//        Class<?> c = strs.getClass().getComponentType();
//        System.out.println("type:" + c.getName());
//        System.out.println("lenth:" + Array.getLength(strs));
//        System.out.println("first content:" + Array.get(strs, 0));


//        Class<?> c1 = null;
//        try {
//            c1 = Class.forName("reflection.zq.basicclass.demo.ClassDemo");
//            Method mth = c1.getMethod("SayHello", String.class, int.class);
//            if(mth != null)
//                mth.invoke(c1.newInstance(), "zhaoq", 18);
//
//        } catch (ClassNotFoundException e) {
//            e.printStackTrace();
//        } catch (NoSuchMethodException e) {
//            e.printStackTrace();
//        } catch (IllegalAccessException e) {
//            e.printStackTrace();
//        } catch (InvocationTargetException e) {
//            e.printStackTrace();
//        } catch (InstantiationException e) {
//            e.printStackTrace();
//        }


        Subject me = new RealMan();
        MyInvocationHandler myProxy = new MyInvocationHandler();
        //返回的是一个什么对象？
//        Proxy worker = myProxy.bind(me);
        Subject su = (Subject)myProxy.bind(me);
        try {
//            Object[] realargs = {"Zhao", 18};
//            myProxy.invoke(new Assitant(), me.getClass().getMethod("Say"), realargs);
//  正确的调用方法
            su.Say("Zhaoq", 18);
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }
    }
}

/**
 * 动态代理还需要再深入理解一下
 * KKB架构师有提到过
 */


class MyInvocationHandler implements InvocationHandler{
    private  Object obj;

    public Object bind(Object o){
        this.obj = o;

        return Proxy.newProxyInstance(o.getClass().getClassLoader(), o.getClass().getInterfaces(), this);
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        Object o = method.invoke(this.obj, args);

        // do some proxy thing
//        proxy.toString();
        System.out.println((String)o);
        System.out.println("其实是我干的。。。");
        return o;
    }
}

interface Subject{
    String Say(String name, int age);
}

class RealMan implements Subject{

    @Override
    public String Say(String name, int age) {
        return name + ", age:" + age + " ,say hello 2 you.. ";
    }
}

class Assitant{
    public void work(){
        System.out.println("我开干了。。。");
    }
}

class ClassDemo{
    private String name;
    private int age;

    public void SayHello(String name, int age){
       System.out.println(name + ", I\'m " + age  + " years old");
    }

    public void run(){
        Class<?> c1 = null;
        Class<?> c2 = null;
        Class c3 = null;

        try {
            c1 = Class.forName("reflection.zq.basicclass.demo.ClassDemo");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        c2 = new ClassDemo().getClass();
        c3 = ClassDemo.class;

        System.out.println(c1.getName());
        System.out.println(c2.getName());
        System.out.println(c3.getName());
    }

}
