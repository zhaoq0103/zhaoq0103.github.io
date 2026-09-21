package cn.zq.basicclass;

public class GenericsDemo {
    public static void main(String[] args){
       Info<Integer> i = new Info<>();
       i.setVar(15);
        funShangxian(i);

       Info<Float> j = new Info<>();
       j.setVar(18.8f);
        funShangxian(j);

//       Info<String> k = new Info<>();
//       k.setVar("hello");
//       funShangxian(k);


       Info<String> k = new Info<>();
       k.setVar("hello");
//       funShangxian(k);
        funXiaxian(k);
    }

    public static void funShangxian(Info<? extends Number>  info){
        System.out.println(info);
    }

    public static void funXiaxian(Info<? super String>  info){
        System.out.println(info);
    }
}


class Info<T>{
//class Info<T extends Number>{
    private T var;

    public T getVar() {
        return var;
    }

    public void setVar(T var) {
        this.var = var;
    }

    @Override
    public String toString() {
        return "Info{" +
                "var=" + var +
                '}';
    }
}