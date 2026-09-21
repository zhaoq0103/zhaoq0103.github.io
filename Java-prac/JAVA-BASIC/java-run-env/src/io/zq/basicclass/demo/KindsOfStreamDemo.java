package io.zq.basicclass.demo;

import java.io.*;

public class KindsOfStreamDemo {
    public static void main(String[] args){
//        BufferedReaderDemo brd = new BufferedReaderDemo();
//        brd.run();

//        PrintStreamDemo psd = new PrintStreamDemo();
//        psd.run();

//        PipeStreamDemo psd = new PipeStreamDemo();
//        psd.run();


        PushbackInputDemo pid = new PushbackInputDemo();
        pid.run();
    }
}

class PushbackInputDemo{
    public void run(){
        String str = "www.baidu.com";
        PushbackInputStream pis = null;
        ByteArrayInputStream bis = new ByteArrayInputStream(str.getBytes());
        pis = new PushbackInputStream(bis);

        int tmp = 0;
        try{
            while ((tmp = pis.read()) != -1){
                if(tmp == '.'){
                    pis.unread(tmp);
                    pis.read();
                }else{
                    System.out.print((char)tmp);
                }
            }
        }catch (Exception e){

        }
    }
}

class BufferReaderExDemo{

}

class BufferedReaderDemo{
    private BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

    public void run(){
        System.out.println("请输入内容：\n");
        String content = null;
        try {
            content = br.readLine();
        } catch (IOException e) {
            e.printStackTrace();
        }

        System.out.println(content);
    }
}


class PrintStreamDemo{
    PrintStream ps = null;

    public void run(){
        try {
            ps = new PrintStream(new FileOutputStream( new File("/Users/zhaoq0103/testfile.txt"), true));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

//        try {
//            ps.write("再写一点吧。。".getBytes());
//            ps.write("好的！".getBytes());
            ps.print("再写一点吧。。");
            ps.print("好的。。");
            ps.println("换个行吧。。");
            ps.close();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
    }
}

class PipeStreamDemo{
    private SendStream ss = null;
    private ReceiveStream rs = null;

    class SendStream implements Runnable{
        private PipedOutputStream pos = new PipedOutputStream();

        public PipedOutputStream getPos() {
            return pos;
        }

        @Override
        public void run() {
            try {
                pos.write("Hello".getBytes());
                pos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    class ReceiveStream implements Runnable{
        public PipedInputStream getPis() {
            return pis;
        }

        private PipedInputStream pis = new PipedInputStream();

        @Override
        public void run() {
            byte[] buff = new byte[1024];
            int iLen = 0;
            try {
                iLen = pis.read(buff);
                pis.close();
            } catch (IOException e) {
                e.printStackTrace();
            }

            System.out.println(new String(buff, 0, iLen));
        }
    }

    public void run(){
        ss = new SendStream();
        rs = new ReceiveStream();
        try {
            /**
             * 两个方向都可以连接
             */
//            rs.getPis().connect(ss.getPos());
            ss.getPos().connect(rs.getPis());
        } catch (IOException e) {
            e.printStackTrace();
        }

        new Thread(ss).start();
        new Thread(rs).start();
    }
}