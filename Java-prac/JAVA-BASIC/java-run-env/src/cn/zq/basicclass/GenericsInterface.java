package cn.zq.basicclass;


interface Infos<T>{
    public T getVar();
}



class InfosImpl<T> implements Infos<T>{

    @Override
    public T getVar() {
        return null;
    }
}



class InfosImpl2 implements Infos<String>{

    @Override
    public String getVar() {
        return null;
    }
}


class InfoImpl<T extends Number>{

}


public class GenericsInterface {

    public static void main(String[] args){

    }

    /**
     * 通过泛型方法返回泛型类的实例化对象，
     * 需要在方法的返回类型声明处明确指定泛型标识
     * @param params
     * @param <T>
     * @return
     */
    public static <T extends Number> InfoImpl<T> infoMaker(T params){
        InfoImpl<T> tmp = new InfoImpl<>();
        return tmp;
    }

    /**
     * 声明中 <T> 限制 print2Infos的两个参数的泛型类型必须一致
     * @param i1
     * @param i2
     * @param <T>
     */
    public static <T> void print2Infos(InfosImpl<T> i1, InfosImpl<T> i2){
        System.out.println();
    }

    public static <T> T[] fun1 (T ...params){
        return params;
    }

    public static <T> void fun2(T param[]){

    }

}
