package io.zq.basicclass.demo;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class ScannerDemo {
    public static void main(String[] args){
        ScannerTwo sct = new ScannerTwo();
        sct.run();

//        ScannerOne sco = new ScannerOne();
//        sco.run();
    }
}

class ScannerTwo{
    public void run(){
        File f = new File("/Users/zhaoq0103/testfile.txt");
        try {
            Scanner sc = new Scanner(f);
            sc.useDelimiter("\n");
            StringBuffer sb = new StringBuffer();
            while (sc.hasNext()){
               sb.append(sc.next()).append("\n");
            }
            System.out.println("content:" + sb);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }
}

 class ScannerOne{
    public void run(){
        Scanner sc = new Scanner(System.in);
        sc.useDelimiter("\n");
        System.out.println("输入数据:\n");
        String str = sc.next();
        System.out.println("输入的数据是：" + str);
    }
}