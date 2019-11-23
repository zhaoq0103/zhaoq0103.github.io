public class HelloWorld {
    public static void main(String[] args){

        //classs 引用传递
//        String str1 = "zhaoq";
//        Assist.referArgsDemo(str1);
//        System.out.println(str1);

//        Assist tool = new Assist();
//        tool.name = "name1";
//        System.out.println("name:" + tool.name);
//        testFun(tool);
//        System.out.println("name:" + tool.name);



        //数组
//        int sorce[] = {67, 89, 87,69,90, 100,75};
//        int sorce[] = {4,5,6,3,2,1};
//        for (int i = 0; i < sorce.length; ++i) {
//            for (int j = 0; j < sorce.length; j++) {
////                for (int j = 0; j < sorce.length-1; j++) {
//                //冒泡是比较两个相邻的数据
////                if(sorce[j] > sorce[j+1]){
////                    int tmp = sorce[j];
////                    sorce[j] = sorce[j+1];
////                    sorce[j+1] = tmp;
//
//                    //很明显，这种方法不行，应该如何处理呢？
////                    Assist.swap(sorce[i], sorce[j]);
////                    Assist.swap2(sorce[i], sorce[j]);
////                }
//
//                //这种比较方法算是冒泡吗？
//                if(sorce[i] > sorce[j]){
//                    int tmp = sorce[i];
//                    sorce[i] = sorce[j];
//                    sorce[j] = tmp;
//                }
//            }

            // 显示过程
//            System.out.println("第" + i + "次排序结果：");
//            for (int j = 0; j < sorce.length; j++) {
//                System.out.print(sorce[j] + " ");
//            }
//            System.out.println();
//        }
//
//        System.out.println("最终排序结果：");
//        for (int i = 0; i < sorce.length; i++) {
//           System.out.print(sorce[i] + " ");
//        }
//        System.out.println();


//        //给我女儿打个乘法口诀表
//        for (int i = 1; i <=9 ; i++) {
//            for (int j = 1; j <= i; j++) {
//                System.out.print( i + "*" + j + "=" + i*j + "\t");
//            }
//            System.out.println();
//        }


        //反码
//        int a = -3;
//        int b = 3;
//        System.out.println("a的反码是：" + (~a));
//        System.out.println("b的反码是：" + (~b));// 正数的反码规定的，不能验证？

        //数据溢出
//        int max = Integer.MAX_VALUE;
//        System.out.println("整数最大值：" + max);
//        System.out.println("整数最大值 + 1：" + (max+1));
//        System.out.println("整数最大值 + 2：" + (max+2));
//        System.out.println("整数最大值 + 2L：" + (max+2L));
//        System.out.println("整数最大值(long) + 2：" + ((long)max+2));

//        System.out.println("Hello, World!");
    }

    public static void testFun(Assist assist){
        assist.name = "ss2";
    }
}


class Assist{
    public String name = "assist";

    public  static void swap(int a, int b){
        int tmp = a;
        a = b;
        b = tmp;
    }

    public static void swap2(Integer a, Integer b){
        Integer tmp = a;
        a = b;
        b = tmp;
    }

    public static void referArgsDemo(String s){
        s = "It's me!";
    }
}