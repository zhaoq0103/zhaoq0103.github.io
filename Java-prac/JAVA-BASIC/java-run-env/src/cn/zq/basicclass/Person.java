package cn.zq.basicclass;

public class Person {
    private String name = "default";
    private int age;


    public Person(){
        System.out.println("a person is made");
    }

    public Person(String name, int age){
        this();
        this.name = name;
        this.age = age;
    }

    public String getInfo(){
        System.out.println("Name:" + this.name + " age:" + this.age);
        return "Name:" + this.name + " age:" + this.age;
    }

    @Override
    public boolean equals(Object obj) {
        if(this == obj) return true;
        if( !(obj instanceof Person)) return false;

        Person t = (Person)obj;
        if(this.name.equals(t.name) && this.age == t.age) return true;

        return false;
    }

    //public 属性时，子类可以覆写，private时不能
    private void overrideDemo(){
       System.out.println("parent -> overrideDemo()");
    }

    public void testOverride(){
        this.overrideDemo();
    }

    class Inner{
        public void print(){
           System.out.println("default name:" + name);
        }
    }

    public static void main(String[] args){
        //overwrite
        Person s = new Student();
        ((Student) s).testOverride();

//        Person zs = new Person("zhangsan", 15);
////        zs.getInfo();
//        //内部类的实例化和访问方法
//        Person.Inner in = zs.new Inner();
//        in.print();
    }
}


class Student extends Person{
//    void overrideDemo(){
//        System.out.println("Student -> overrideDemo()");
//    }

    public void overrideDemo(){
        System.out.println("Student -> overrideDemo()");
    }
}
