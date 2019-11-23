package zq.basicclass.demo;

/**
 * 抽象类的实例化:不能实例化，只能用子类实现
 */

abstract class Animal{
    private String name;
    private String color;

    abstract void run();



    public static Animal getAnimalInstance(){
        //相当于一个匿名子类或者实现了抽象类的具象类
        return new Animal() {
            @Override
            void run() {
            }
        };
    }

    public static void doSomething(){
        System.out.println("I'm working...");
    }


    public void doSomethingElse(){
        System.out.println("I'm working yet...");
    }
}


public class AbstractClassInstance {
    public static void main(String[] args){
        Animal a = Animal.getAnimalInstance();
        a.doSomethingElse();
        Animal.doSomething();

    }
}
