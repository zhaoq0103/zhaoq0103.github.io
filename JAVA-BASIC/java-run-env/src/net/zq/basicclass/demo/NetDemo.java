package net.zq.basicclass.demo;

import java.io.*;
import java.net.*;
import java.util.Scanner;

public class NetDemo {
    public static void main(String[] args){

        new Thread(new Runnable() {
            @Override
            public void run() {
                Server s = new Server();
                s.run();
            }
        }).start();

        new Thread(new Runnable() {
            @Override
            public void run() {
                Client c = new Client();
                c.run();
            }
        }).start();




//        URLDemo ud = new URLDemo();
//        ud.run();

//        InetAddressDemo iad = new InetAddressDemo();
//        iad.run();
    }
}


class Server{
    public void run(){
        try {
            ServerSocket server = new ServerSocket(8888);
            System.out.println("Sever waiting:\n");
            Socket client = server.accept();
            System.out.println("来了一个客人。。\n");
            PrintStream os = new PrintStream(client.getOutputStream());
            os.println("Hello, Client, I heared you..");
            os.close();
            client.close();
            server.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}

class Client{
    public void run(){
        try {
            Socket s = new Socket("localhost", 8888);
            BufferedReader br = new BufferedReader( new InputStreamReader( s.getInputStream()));
            String str =  br.readLine();
            System.out.println("服务器来信说：" + str);

            br.close();
            s.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}

class URLDemo{
    public void run(){
        String str = "Hello, 感谢访问...666";
        try {
            String strEn =  URLEncoder.encode(str, "UTF-8");
            System.out.println("encode:" + strEn);
            String strDe = URLDecoder.decode(strEn, "UTF-8");
            System.out.println("decode:" + strDe);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }


//        try {
//            URL url = new URL("http://www.baidu.com");
//            URLConnection uc = url.openConnection();
//            int iLen = uc.getContentLength();
//            String strType = uc.getContentType();
//            System.out.println("iLen:" + iLen + " type:" + strType);
//        } catch (MalformedURLException e) {
//            e.printStackTrace();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }


//        try {
//            URL url = new URL("http", "www.baidu.com",80,"/index.html");
//            InputStream is = url.openStream();
//            Scanner sc = new Scanner(is);
//            sc.useDelimiter("\n");
//            while (sc.hasNext()){
//                System.out.println(sc.next());
//            }
//        } catch (MalformedURLException e) {
//            e.printStackTrace();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
    }
}

class InetAddressDemo{
    private InetAddress localAddr = null;
    private InetAddress remoteAddr = null;

    public void run(){
        try {
            localAddr = InetAddress.getLocalHost();
            remoteAddr = InetAddress.getByName("www.baidu.com");
            boolean isReachable = remoteAddr.isReachable(3000);
            System.out.println("local ip :" + localAddr.getHostAddress());
            System.out.println("remote ip:" + remoteAddr.getHostAddress());
            System.out.println("local reachable:" + localAddr.isReachable(3000));
            System.out.println("remote reachable:" + isReachable);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}