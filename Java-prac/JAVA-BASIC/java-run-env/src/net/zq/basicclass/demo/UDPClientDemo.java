package net.zq.basicclass.demo;

import java.io.IOException;
import java.net.*;

public class UDPClientDemo {

    public static void main(String[] args){
        DatagramSocket ds = null;
        DatagramPacket dp = null;

        byte[] buff = new byte[1024];
        try {
            ds = new DatagramSocket(9000);
            dp = new DatagramPacket(buff, 1024);

            System.out.println("Waiting for data...\n");
            ds.receive(dp);
            String str = new String(dp.getData(), 0, dp.getLength()) + " from " + dp.getAddress().getHostAddress() + " : " + dp.getPort();
            System.out.println(str);
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
