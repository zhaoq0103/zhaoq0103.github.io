package net.zq.basicclass.demo;

import java.io.*;
import java.net.Socket;

public class EchoClient{

    public static void main(String[] args) {
        Socket cc = null;
        BufferedReader input = null;
        PrintStream osw = null;
        BufferedReader brcc = null;
        try {
            cc = new Socket("localhost", 8888);
            input = new BufferedReader(new InputStreamReader(System.in));
            osw = new PrintStream(cc.getOutputStream());
            brcc = new BufferedReader( new InputStreamReader(cc.getInputStream()));

            boolean flag = true;
            while (flag){
                System.out.println("Please Say Something:\n");
                String ss = input.readLine();
                if(ss == null || ss.trim().equals("")){
                    continue;
                }

                if (ss.trim().equals("quit")){
                    flag = false;
                }else{
                    osw.println(ss);
                    String echoStr = brcc.readLine();
                    System.out.println(echoStr);
                }
            }

            input.close();
            osw.close();
            brcc.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
