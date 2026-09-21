package reflection.zq.basicclass.demo;

import java.lang.annotation.*;
import java.lang.reflect.Method;

import static java.lang.annotation.RetentionPolicy.SOURCE;

@MyDefaultAnnotationNoParam
@MyDefaultAnnotationOneParam(param="赵强")
@MyDefaultAnnotationNParam(param = "zhao", key = "name",value = "qiang")
@MyDefaultAnnotationArrayParam(array = {"Hello", "World"})
@MyDefaultAnnotationValue
@MyDefaultAnnotationEnum(color = Color.BLUE)
public class AnnotationDemo {
    private String name;

    public static void main(String[] args){
        ReflectAnnotationDemo rad = new ReflectAnnotationDemo();
        rad.sayHello();

//        Class<?> clazz = ReflectAnnotationDemo.class;
        try {
            Class<?> clazz = Class.forName("reflection.zq.basicclass.demo.ReflectAnnotationDemo");
            Method m = clazz.getMethod("sayHello");
//            Annotation[]  as =  m.getDeclaredAnnotations();
//            for (Annotation a : as ) {
//               System.out.println(a);
//            }

            // Annotation的使用， 如果annotation不标记为RUNTIME,运行时没有作用
            if(m.isAnnotationPresent(MyDefaultAnnotationOneParam.class)){
                Annotation an = m.getAnnotation(MyDefaultAnnotationOneParam.class);
                String s = ((MyDefaultAnnotationOneParam) an).param();
                System.out.println("get annotation:" + s);
            }

            if(clazz.isAnnotationPresent(MyDefaultAnnotationOneParam.class)){
                System.out.println("yes, annotation..");
            }
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}


class ReflectAnnotationDemo{

    @SuppressWarnings("unchecked")
    @Deprecated
    @MyDefaultAnnotationOneParam(param = "zhaoq")
    public String sayHello(){
        return "Hello";
    }
}


class Zhaoq implements Annotation{

    @Override
    public Class<? extends Annotation> annotationType() {
        return null;
    }



}

/**
 * 第一种方法自定义 Annotation
 */
@interface MyDefaultAnnotationNoParam{

}

/**
 * 第二种方法自定义 Annotation， 有一个参数
 */
@Target({ElementType.METHOD,ElementType.TYPE})
@Retention(value = RetentionPolicy.RUNTIME)
@interface MyDefaultAnnotationOneParam{
//    public String value(); //默认参数名
    public String param();
}


/**
 * 第三种方法自定义 Annotation， 有一个参数
 */

@interface MyDefaultAnnotationNParam{
    public String param();
    public String key();
    public String value();
}


/**
 * 第四种方法自定义 Annotation， 有一个参数
 */

@interface MyDefaultAnnotationArrayParam{
    public String[] array();
}

/**
 * 第五种方法自定义 Annotation，默认值
 */

@interface MyDefaultAnnotationValue{
    public String key() default "name";
    public String value() default "qiang";
}

/**
 * 第六种方法自定义 Annotation，默认值
 */

enum Color{
    RED,BLUE,YELLOE;
}

@Retention(value = RetentionPolicy.CLASS)
@interface MyDefaultAnnotationEnum{
    public Color color() default Color.RED;
}



