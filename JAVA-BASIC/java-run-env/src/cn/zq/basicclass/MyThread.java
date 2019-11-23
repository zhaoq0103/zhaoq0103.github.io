package cn.zq.basicclass;

/**
 * Object类对线程的支持：
 * final void wait();
 * final void notify();
 * final void notifyAll();
 */
public class MyThread extends Thread {

    @Override
    public void run() {
        for (int i = 0; i < 10; i++) {
           System.out.println(Thread.currentThread().getName() + " running " + i);
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public static void main(String[] args){
        MyThread mt = new MyThread();
        Thread thread =  new Thread(mt, "子线程");
        thread.start();

        for (int i = 0; i < 10; i++) {
           if(i > 3){
               try {
                   thread.join(); //强制子thread运行
               } catch (InterruptedException e) {
                   e.printStackTrace();
               }
           }
           System.out.println(Thread.currentThread().getName()  + " running..." + i);
        }

//       MyThread mt = new MyThread();
//       new Thread(mt, "子线程-").start();
//       System.out.println(Thread.currentThread().getName() + " 主线程从这里结束了");
    }
}
