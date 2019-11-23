package cn.zq.basicclass;

interface iPersonInfo{
}

class Contact implements iPersonInfo{
    private String address;
    private String tel;
    private String mail;
}

class Introduction implements iPersonInfo{
    private String name;
    private String position;
}

class  PersonInfo <T extends iPersonInfo>{
    private T info;
    public PersonInfo(T info){
        this.info = info;
    }
}

public class GenericsApp {
    public static void main(String[] args){
        PersonInfo<Contact> p1 = new PersonInfo<>(new Contact());
        PersonInfo<Introduction> p2 = new PersonInfo<>(new Introduction());
    }
}
