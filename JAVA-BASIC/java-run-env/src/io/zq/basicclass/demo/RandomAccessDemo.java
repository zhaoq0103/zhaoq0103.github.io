package io.zq.basicclass.demo;

import java.io.*;

public class RandomAccessDemo {
    public static void main(String[] args){
        ByteArrayStreamDemo bas = new ByteArrayStreamDemo();
        bas.toUpper();


//        File testFile = new File("/Users/zhaoq0103/testfile.txt");
//        Writer wo = null;
//        try {
//            wo = new OutputStreamWriter(new FileOutputStream(testFile, true));
//            wo.write("\r\n大家好，才是真的好...\r\n");
//            wo.close();
//        } catch (FileNotFoundException e) {
//            e.printStackTrace();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }


//        FileCopyer fc = new FileCopyer();
//        fc.fileCopy("/Users/zhaoq0103/testfile.txt", "/Users/zhaoq0103/testfile2.txt");

//        File testFile = new File("/Users/zhaoq0103/testfile.txt");
//        FileOutputStream fos = null;
//        FileInputStream fis = null;
//        Writer out = null;
//        //和FileOutputStream类似
//        Reader in = null;
//        try {
////            fos = new FileOutputStream(testFile);
////            fos = new FileOutputStream(testFile, true);
////            fos.write("Hello, zhaoq0103!".getBytes());
////            fos.write("我陪你坐坐".getBytes());
////            fos.write("\r\n".getBytes());
////            fos.close();
//
////            long fLen = testFile.length();
////            fis = new FileInputStream(testFile);
////            byte[] con = new byte[(int)fLen];
////            int len = fis.read(con);
////            fis.close();
////            System.out.println("con:" + new String(con, 0, len) );
//
////            out = new FileWriter(testFile);
//            out = new FileWriter(testFile, true);
//            out.write("孤独是永恒的..\r\n");
//            out.append("Are you OK?\r\n");
//            out.close();
//
//        } catch (FileNotFoundException e) {
//            e.printStackTrace();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }finally {
//        }
    }
}

class ByteArrayStreamDemo{
    private String str = "HELLO,ZHAOQIANG";
    ByteArrayInputStream bais = null;
    ByteArrayOutputStream baos = null;

    public void toUpper(){
        bais = new ByteArrayInputStream(str.getBytes());
        baos = new ByteArrayOutputStream();
        int iRead = 0;
        while ((iRead = bais.read()) != -1){
            char c = (char)iRead;
//            baos.write(c - ('a' - 'A'));
            baos.write(Character.toLowerCase(c));
        }

        String newStr = baos.toString();

        try {
            bais.close();
            baos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        System.out.println(newStr);
        System.out.println(str);
    }
}

class FileCopyer{
    File fo = null;
    File fi = null;
    private FileOutputStream fos = null;
    private FileInputStream fis = null;

    public void fileCopy(String scrFilePath, String destFilePath){
        fi = new File(scrFilePath);
        try {
            fis = new FileInputStream(fi);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        //这个抛出异常有点奇怪，不应该是创建文件吗？
        fo = new File(destFilePath);
        try {
            fos = new FileOutputStream(fo);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        assert  ((fis != null) && (fos != null));

        /**
         * fos.write(iRead) 如何知道写的是哪部分数据?
         */
        int iRead = 0;
        try{
            while ((iRead = fis.read()) != -1){
                fos.write(iRead);
            }
        }catch (Exception e){

        }


        try {
            fis.close();
            fos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        System.out.println("File copy finished!");
    }
}