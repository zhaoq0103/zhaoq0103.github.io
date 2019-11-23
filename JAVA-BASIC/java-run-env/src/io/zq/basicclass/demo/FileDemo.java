package io.zq.basicclass.demo;

import java.io.File;
import java.io.IOException;

public class FileDemo {
    public  static void main(String[] args){

//        String filePath = ".";
        String filePath = "/Users/zhaoq0103/test";
        DirDemo dirDemo = new DirDemo();
        dirDemo.run(filePath);

////        String path = File.separator + "Users" + File.separator + "zhaoq0103" + File.separator + "testfile001.txt";
//        String path = File.separator + "Users" + File.separator + "zhaoq0103";
////        File f = new File("/Users/zhaoq0103/testfile01.txt");
//        File f = new File(path);
//        try {
////            String[] files = f.list();
//            File[] files = f.listFiles();
//            for (int i = 0; i < files.length; i++) {
//                System.out.println(files[i]);
//            }
//
////            f.createNewFile();
//
//
////            if(f.exists())
////                f.delete();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }

//        System.out.println(File.separator);
//        System.out.println(File.pathSeparator);
    }
}

class DirDemo{

    public void run(String path){
       String filePath = path;
       File f = new File(path);

       listDir(f);
    }

    private void listDir(File f) {
        if(f != null){
            if(f.isDirectory()){

                File[] files = f.listFiles();
                if(files != null){
                    if(files.length == 0){//empty dir
                        System.out.println(f);
                    }
                    for (int i = 0; i < files.length; i++) {
                       listDir(files[i]);
                    }
                }
            }else{
                System.out.println(f);
            }
        }
    }
}