package net.zq.basicclass.demo;

import java.io.IOException;
import java.net.*;

public class UDPServerDemo {
    public static void main(String[] args){
        DatagramSocket ds = null;
        DatagramPacket dp = null;

        try {
            ds = new DatagramSocket(3000);
            String str = "Hello, World!";
            dp = new DatagramPacket(str.getBytes(), 0, str.length(), InetAddress.getByName("localhost"), 9000);

            System.out.println("Send Msg..\n");
            ds.send(dp);
            ds.close();
        } catch (SocketException e) {
            e.printStackTrace();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
