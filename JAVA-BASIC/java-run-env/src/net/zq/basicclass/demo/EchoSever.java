package net.zq.basicclass.demo;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class EchoSever {
    public static void main(String[] args){
        ServerSocket ss = null;
        Socket cc = null;
        try {
            ss = new ServerSocket(8888);
            while (true) {
                System.out.println("Sever is Ready:\n");
                cc = ss.accept();
                new Thread(new EchoSeverThread(cc)).start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        if(ss != null) {
            try {
                ss.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}

class EchoSeverThread implements Runnable{
    private Socket cc = null;
    public EchoSeverThread(Socket cc){
        this.cc = cc;
    }

    @Override
    public void run() {
        BufferedReader br = null;
        PrintStream os = null;
        try {
                br = new BufferedReader( new InputStreamReader( cc.getInputStream()));
                os = new PrintStream(cc.getOutputStream());

                boolean flag = true;
                while (flag){
                    String strCCInfo = br.readLine();
                    if(strCCInfo == null || strCCInfo.trim().equals("")){
                        flag = false;
                    }else{
                        if(strCCInfo.equals("quit")){
                            flag = false;
                        }

                        System.out.println("Echo: " + strCCInfo);
                        os.println("Echo: " + strCCInfo);
                    }
                }

                os.close();
                cc.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}