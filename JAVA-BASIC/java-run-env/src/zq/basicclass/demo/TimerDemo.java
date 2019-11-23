package zq.basicclass.demo;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

public class TimerDemo {
    public static void main(String[] args){
        Timer t = new Timer();
        t.schedule(new MyTimerTask(), 1000, 2000);
    }

}


class MyTimerTask extends TimerTask{

    @Override
    public void run() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        System.out.println("时间：" + sdf.format(new Date()));
    }
}