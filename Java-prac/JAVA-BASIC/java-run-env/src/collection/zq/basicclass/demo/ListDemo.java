package collection.zq.basicclass.demo;

import java.io.Serializable;
import java.util.*;

public class ListDemo {
    public static void main(String[] args){
//        List<String> all = new ArrayList<>();
//        all.add("Hello");
//        all.add("World");
//        //这种语法以前没太用过
//        String[] strs = all.toArray(new String[]{});
//        for (String s : strs ) {
//           System.out.print(s + "\t");
//        }


//        LinkedListDemo lld = new LinkedListDemo();
//        lld.run();


//        HashSetDemo hsd = new HashSetDemo();
//        hsd.run();


//        SortedSetDemo ssd = new SortedSetDemo();
//        ssd.run();

        HashMapDemo hmd = new HashMapDemo();
        hmd.run();
        Class clazz = hmd.getClass();
    }
}

class HashMapDemo{
    /**
     * 自定义对象作map的key, 需要重写 equals() 和 hashCode() 两个方法，用来区分不同对象
     */
    public void run(){
        HashMap<Person, String> map = new HashMap<>();
        map.put(new Person("张三",20), "zhangsan");
        map.put(new Person("张四",21), "zhangsi");
        map.put(new Person("李四",21), "zhangsi");
        map.put(new Person("张五",22), "zhangwu");
        map.put(new Person("张五",26), "zhangwu");
        map.put(new Person("张六",23), "zhangliu");
        map.put(new Person("张六",23), "zhangliu");

        System.out.println(map.get(new Person("李四",21)));

    }
}


class Person implements Serializable {
    private String name;
    private int age;

    public Person(String name, int age) {
        this.name = name;
        this.age = age;
    }

    @Override
    public boolean equals(Object obj) {
        if(this == obj) return true;
        if(! (obj instanceof Person)) return false;

        Person tmp = (Person)obj;
        if (this.name.equals(tmp.name) &&(this.age == tmp.age)){
            return true;
        }
        return false;
    }

    @Override
    public int hashCode() {
        return this.name.hashCode() * this.age;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }


}


class SortedSetDemo{
    public void run(){
        /**
         * 不知道这个headset 和 tailset 干嘛用
         */
       SortedSet<String>  allSet = new TreeSet<>();
        allSet.add("A");
        allSet.add("C");
        allSet.add("D");
        allSet.add("C");
        allSet.add("B");
        allSet.add("C");
        allSet.add("E");

//        System.out.println("first e:" + allSet.first());
//        System.out.println("last e:" + allSet.last());
//        System.out.println("headset e:" + ((TreeSet<String>) allSet).headSet("C"));
//        System.out.println("tail e:" + ((TreeSet<String>) allSet).tailSet("C"));
//        System.out.println("subset e:" + allSet.subSet("B", "D"));


        Iterator<String> it = allSet.iterator();
//        ListIterator<String> lli = allSet.listIterator();
        while (it != null && it.hasNext()){
            System.out.print(it.next() + "\t");
        }
    }
}

class HashSetDemo{
    public void run(){
        Set<String> set = new HashSet<>();
//        Set<String> set = new TreeSet<>();
        set.add("A");
        set.add("C");
        set.add("D");
        set.add("C");
        set.add("B");
        set.add("C");
        set.add("E");
        System.out.println(set);
    }
}

class LinkedListDemo{
    public void run(){
        List<String> list = new LinkedList<>();
        list.add("A");
        list.add("B");
        list.add("C");
        System.out.println("init list:" + list);

        ((LinkedList<String>) list).addFirst("X");
        ((LinkedList<String>) list).addLast("Y");
        System.out.println("update list:" + list);

    }
}