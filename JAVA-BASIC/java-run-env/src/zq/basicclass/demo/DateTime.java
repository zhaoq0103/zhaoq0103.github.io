package zq.basicclass.demo;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DateTime {
    private SimpleDateFormat sdf = null;
    private String simpleFormatStr = "yyyy-MM-dd HH:mm:ss";
    private String completeFormatStr = "yyyy年MM月dd日 HH时mm分ss秒";

    public String getDate(){
        sdf = new SimpleDateFormat(simpleFormatStr);
        return sdf.format(new Date());
    }

    public String getCompleteDate(){
        sdf = new SimpleDateFormat(completeFormatStr);
        return sdf.format(new Date());
    }


    public static void main(String[] args){
        DateTime dt = new DateTime();
        System.out.println("系统时间：" + dt.getDate());
        System.out.println("中文时间：" + dt.getCompleteDate());
    }
}
