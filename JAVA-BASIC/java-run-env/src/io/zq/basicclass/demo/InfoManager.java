package io.zq.basicclass.demo;

import java.io.*;

public class InfoManager {
    public static void main(String[] args){
        Operate.add();
        Operate.find();
    }
}


class FileOperate{
    private File file = null;

    public FileOperate(String filePath){
        this.file = new File(filePath);
    }

    public boolean save(Object obj){
        boolean result = false;
        ObjectOutputStream oos = null;
        try {
            oos = new ObjectOutputStream(new FileOutputStream(this.file));
            oos.writeObject(obj);
            result = true;
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            if(oos != null) {
                try {
                    oos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        return result;
    }

    public Object load(){
        Object o = null;
        ObjectInputStream oos = null;
        try {
            oos = new ObjectInputStream(new FileInputStream(this.file));
            o = oos.readObject();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }finally {
            if(oos != null) {
                try {
                    oos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return o;
    }
}


class Person implements Serializable{
    private String name;
    private int age;

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

class Operate{
    public static void add(){
        System.out.println("add person");
    }
    public static void delete(){
        System.out.println("delete person");

    }
    public static void find(){
        System.out.println("find person");

    }
    public static void update(){
        System.out.println("update person");

    }

}


class InputData{

}