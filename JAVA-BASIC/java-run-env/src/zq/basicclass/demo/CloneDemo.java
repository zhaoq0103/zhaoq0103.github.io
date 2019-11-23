package zq.basicclass.demo;

/**
 * Clone是怎样实现的深拷贝？
 */
public class CloneDemo {

    public static void main(String[] args){
        Person p3 = new Person();
        p3.setName("zhangsan");
        Person p4 = null;
        try {
            p4 = (Person) p3.clone();
            p4.setName("lisi");
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }

        System.out.println("p3:" + p3);
        System.out.println("p4:" + p4);
    }
}

class Person implements Cloneable{
    private String name ;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


    @Override
    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }

    @Override
    public String toString(){
        return "name:" + name;
    }
}