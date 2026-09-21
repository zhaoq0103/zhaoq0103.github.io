package cn.zq.basicclass;

public class MyException {
    public int div(int a, int b) throws Exception{
        int result;
        try {
            result = a / b;
        }catch (Exception e){
            throw e;
        }finally {

        }

        return result;
    }

    /**
     * RuntimeException可以不用try catch, JVM会处理
     */
    public void runtimeException(){
        String s = "123";
        int tmp = Integer.parseInt(s);
        System.out.println("string 2 int:" + tmp);
    }


    public static void main(String[] args){
        MyException e = new MyException();
//        try {
//            e.div(10, 0);
//        } catch (Exception e1) {
//            e1.printStackTrace();
//        }

        assert  e == null : "e没有赋值";

        e.runtimeException();

    }
}
