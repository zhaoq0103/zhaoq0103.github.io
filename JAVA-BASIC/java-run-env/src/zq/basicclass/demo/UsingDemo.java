package zq.basicclass.demo;

import java.text.DateFormat;
import java.text.MessageFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Enumeration;
import java.util.Locale;
import java.util.ResourceBundle;

public class UsingDemo {

    public static void main(String[] args){

        /**
         * 时间类型转换经常在数据库中使用
         */
//        DateFormat df  = DateFormat.getDateTimeInstance();
//        System.out.println("Date:" + df.format(new Date()));
//
//        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//        SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy年MM月dd日 HH时mm分ss秒");
//        Date d = null;
//        try {
//            d = sdf.parse("2019-11-13 6:19:24");
//        } catch (ParseException e) {
//            e.printStackTrace();
//        }
//        System.out.println(sdf2.format(d));

//        System.getProperties().list(System.out);

//        Locale zhLoc = new Locale("zh", "CN");
//        ResourceBundle rb = ResourceBundle.getBundle("Message", zhLoc);
//        String s = rb.getString("info");
//        s = MessageFormat.format(s, "赵强");
//        System.out.println(s);
//
//        Locale enLoc = new Locale("en", "US");
//        ResourceBundle rben = ResourceBundle.getBundle("Message", enLoc);
//        String sen = rben.getString("info");
//        sen = MessageFormat.format(sen, "James Bond");
//        System.out.println(sen);

//        System.out.println(rb.getString("info2"));

//        Runtime rt = Runtime.getRuntime();
//        System.out.println("max jvm mem:" + (rt.maxMemory()/1024.0/1024.0));




        /**
         * StringBuffer
         */
        //        StringBuffer sb = new StringBuffer();
//        sb.append("Hello ");
//        sb.append("World").append("!!!").append("\n");
//        String s = sb.reverse().toString();
//        System.out.println(s);
//
//        sb.append("数字：").append(666).append("\n");
//
//        System.out.println(sb);
    }
}
