import com.sun.xml.internal.ws.api.ha.StickyFeature;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.io.UnsupportedEncodingException;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.*;
import java.util.function.LongSupplier;
import java.util.function.Supplier;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.LongStream;
import java.util.stream.Stream;


@HelloAnnotation(say = "Do it!")
public class Main {
    public static void main(String[] args)  {



//        Stream.generate(new LocalDateSupplier())
//                .limit(40)
//                .filter(day -> day.getDayOfWeek() == DayOfWeek.SATURDAY || day.getDayOfWeek() == DayOfWeek.SUNDAY)
//                .forEach(System.out::println);
    }

//    <R> Stream<R> map(Function<? super T, ? extends R> mapper);
//    1, 1, 2, 3, 5, 8, 13, 21, 34, ...


//    static void testMap(){
//        // 按行读取配置文件:     >= JDK9   List.of()
//        List<String> props = List.of("profile=native", "debug=true", "logging=warn", "interval=500");
//        Map<String, String> map = props.stream()
//                // 把k=v转换为Map[k]=v:
//                .map(kv -> {
//                    String[] ss = kv.split("\\=", 2);
//                    return Map.of(ss[0], ss[1]);
//                })
//                // 把所有Map聚合到一个Map:
//                .reduce(new HashMap<String, String>(), (m, kv) -> {
//                    m.putAll(kv);
//                    return m;
//                });
//        // 打印结果:
//        map.forEach((k, v) -> {
//            System.out.println(k + " = " + v);
//        });
//    }


    static void proxyDoJob(){
        InvocationHandler handler = new InvocationHandler() {
            @Override
            public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                System.out.println(method);
                System.out.println(proxy.getClass());
                if (method.getName().equals("morning")) {
                    System.out.println("Good morning, " + args[0]);
                }
                return null;
            }
        };
        Hello hello = (Hello) Proxy.newProxyInstance(
                Hello.class.getClassLoader(), // 传入ClassLoader
                new Class[] { Hello.class }, // 传入要实现的接口
                handler); // 传入处理调用方法的InvocationHandler
        hello.morning("Bob");
    }

    static public void proxyDoJobEx(){
        InvocationHandler handler = new worldInvocationHandler<World>(new World());
        iW world = (iW) Proxy.newProxyInstance(
                iW.class.getClassLoader(), // 传入ClassLoader
                new Class[] { iW.class }, // 传入要实现的接口
                handler); // 传入处理调用方法的InvocationHandler
        world.helloWorld();
    }
    interface Hello {
        void morning(String name);
    }

    static void printClassInfo(Class cls) {
        System.out.println("Class name: " + cls.getName());
        System.out.println("Simple name: " + cls.getSimpleName());
        if (cls.getPackage() != null) {
            System.out.println("Package name: " + cls.getPackage().getName());
        }
        System.out.println("is interface: " + cls.isInterface());
        System.out.println("is enum: " + cls.isEnum());
        System.out.println("is array: " + cls.isArray());
        System.out.println("is primitive: " + cls.isPrimitive());
        System.out.println(" =============== ");

        try {
            Class.forName("hello");
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }


    }

    static String buildInsertSql(String table, String[] fields) {
        StringJoiner sj = new StringJoiner(", ", "INSERT INTO "+table+" (", ") VALUES (?, ?, ?)");
        for (String field : fields) {
            sj.add(field);
        }
        return sj.toString();
    }

    static public  void testString()  {
        try {
            byte[] b1 = "Hello".getBytes(); // 按系统默认编码转换，不推荐
            byte[] b2 = "Hello".getBytes("UTF-8"); // 按UTF-8编码转换
            byte[] b3 = "Hello".getBytes("GBK"); // 按GBK编码转换
            byte[] b4 = "Hello".getBytes(StandardCharsets.UTF_8); // 按UTF-8编码转换


            byte[] b11 = "中".getBytes(); // 按系统默认编码转换，不推荐
            byte[] b22 = "中".getBytes("UTF-8"); // 按UTF-8编码转换
            byte[] b33 = "中".getBytes("GBK"); // 按GBK编码转换
            byte[] b44 = "中".getBytes(StandardCharsets.UTF_8); // 按UTF-8编码转换

            int i = 9;
        }catch (Exception e){

        }

    }

    static String  toHex(int num){
        if (num == 0) return "0";

        int num2 = num;
        String res = "";
        String hex[] = {"0","1","2","3","4","5","6","7","8","9","a","b","c","d","e","f"};
        while (num2 > 0){{
            res = hex[num2 %16] + res;
            num2 /= 16;
        }}
//        res = hex[num2 - ((num2 >> 4) << 4)] +res;

        return  res;
    }

    static  public String unicodeEncode(String string){
        char[] utfBytes = string.toCharArray();
        String unicodeBytes = "";
        for (int i = 0; i < utfBytes.length; i++) {
            String hexB = Integer.toHexString(utfBytes[i]);
            if(hexB.length() <=2 ){
                hexB = "00" + hexB;
            }

            unicodeBytes = unicodeBytes + "\\u" + hexB;
        }
        return  unicodeBytes;
    }

    static  public String unicodeDecode(String string){
        Pattern pattern = Pattern.compile("(\\\\u(\\p{XDigit}{4}))");
        Matcher matcher = pattern.matcher(string);
        char ch;
        while (matcher.find()){
            ch = (char) Integer.parseInt(matcher.group(2),16);
            string  = string.replace(matcher.group(1), ch + "");
        }

        return  string;
    }
}

class LocalDateSupplier implements Supplier<LocalDate> {
    LocalDate start = LocalDate.of(2022, 8, 1);
    int n = -1;
    public LocalDate get() {
        n++;
        return start.plusDays(n);
    }
}


class FibSupplier implements LongSupplier {
    long first = 0;
    long second = 1;
    long result = 0;

    public long getAsLong() {
        result = first + second;
        first = second;
        second = result;
        return result;
    }
}

class NatualSupplier implements Supplier<Integer> {
    int first = 0;
    int second = 1;
    int result = 0;
    public Integer get() {
        result = first + second;
        first = second;
        second = result;
        return result;
    }
}


//CGLIB代理
//导入cglib的jar包,另外Spring的核心包中已经包括了Cglib功能,也可以导入spring-core-xxx.jar
// todo


class worldInvocationHandler<T> implements InvocationHandler {
    private T slf;
    public worldInvocationHandler(T arg){
        this.slf = arg;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        System.out.println("I see you..");
        Object ob = method.invoke(slf, args);
        System.out.println("88..");
        return null;
    }
}

interface  iW{
    public void  helloWorld();
}
class World implements iW{
    @Override
    public void helloWorld(){
        System.out.println("hello, world!");
    }
}


class Person {
    public void hello() {
        System.out.println("Person:hello");
    }

    public void hello(String name) {
        System.out.println("Person:hello " + name);
    }

    public void hello(String firstName, String lastName) {
        System.out.println("Person:hello " + firstName + "  " + lastName);
    }
}

class Student extends Person {
    public void hello() {
        System.out.println("Student:hello");
    }

    public void hello(String name) {
        System.out.println("Person:hello " + name);
    }

    public void hello(String firstName, String lastName) {
        System.out.println("Person:hello " + firstName + "  " + lastName);
    }
}



class Score {
    private int[] scores;

    public Score(int[] scores) {
        int len = scores.length;
        if (len > 0){
            this.scores =new int[len];

            for (int i = 0; i < len; i++) {
                this.scores[i] = scores[i];
            }
        }
    }

    public void printScores() {
        System.out.println(Arrays.toString(scores));
    }
}

//class Person {
////    public static int number;
////
////    public static void setNumber(int number) {
////        Person.number = number;
////    }
//
//    private String name;
//    private int age;
//
//    public String getName() {
//        return name;
//    }
//
//    public void setName(String name) {
//        this.name = name;
//    }
//
//    public int getAge() {
//        return age;
//    }
//
//    public void setAge(int age) {
//        this.age = age;
//    }
//
//}