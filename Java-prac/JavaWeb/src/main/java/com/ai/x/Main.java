package com.ai.x;

import jakarta.servlet.ServletException;
import org.apache.catalina.Context;
import org.apache.catalina.WebResourceRoot;
import org.apache.catalina.startup.Tomcat;
import org.apache.catalina.webresources.DirResourceSet;
import org.apache.catalina.webresources.StandardRoot;
import org.apache.tomcat.util.http.fileupload.ByteArrayOutputStream;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.security.DigestInputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class Main {


    public static void main(String[] args) throws Exception {
//            startTomcat();
            testFunction();
    }


    private static  void testFunction(){
        String msg = "file-data,19 length";
        String digest = "SHA-1";
        String signature = "8ce8b26a467453e4e0506020ec4d9c0e422dff6e";


        try {
            //验证签名:
            MessageDigest md = getMessageDigest(digest);
//            InputStream input = new DigestInputStream(request.getInputStream(), md);
//            这种方法不行，不确定原因
//            InputStream input = new DigestInputStream(new ByteArrayInputStream(msg.getBytes("UTF-8")),md);

            md.update(msg.getBytes("UTF-8"));

            String actual = toHexString(md.digest());
            if (!signature.equals(actual)) {
                System.out.println("error happened.");
                return;
            }else{
                System.out.println("checked!!");
            }
        } catch (Exception e) {
            System.out.println(e);
        }
    }


    private static String toHexString(byte[] digest) {
        StringBuilder sb = new StringBuilder();
        for (byte b : digest) {
            sb.append(String.format("%02x", b));
        }
        return sb.toString();
    }

    private static MessageDigest getMessageDigest(String name){
        try {
            return MessageDigest.getInstance(name);
        } catch (NoSuchAlgorithmException e) {
            System.out.println(e);
            return null;
        }
    }

    private  static void startTomcat() throws  Exception{
        // 启动Tomcat:
        Tomcat tomcat = new Tomcat();
        tomcat.setPort(Integer.getInteger("port", 8080));
        tomcat.getConnector();
        // 创建webapp:
        Context ctx = tomcat.addWebapp("", new File("src/main/webapp").getAbsolutePath());
        WebResourceRoot resources = new StandardRoot(ctx);
        resources.addPreResources(
                new DirResourceSet(resources, "/WEB-INF/classes", new File("target/classes").getAbsolutePath(), "/"));
        ctx.setResources(resources);
        tomcat.start();
        tomcat.getServer().await();
    }



}
