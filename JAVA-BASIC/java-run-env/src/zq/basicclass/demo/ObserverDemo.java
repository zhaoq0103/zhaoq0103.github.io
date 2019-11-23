package zq.basicclass.demo;

import java.util.Observable;
import java.util.Observer;


//这个deprecated了， 可以点击进去查看说明
//这个可以做成同步通知和异步通知两种
public class ObserverDemo {
    public static void main(String[] args){
        House hs = new House();
        HouseHolder hh1 = new HouseHolder("A");
        HouseHolder hh2 = new HouseHolder("B");
        HouseHolder hh3 = new HouseHolder("C");
        hs.addObserver(hh1);
        hs.addObserver(hh2);
        hs.addObserver(hh3);

        hs.setPrice(666);
        System.out.println("a month later....");
        hs.setPrice(888);
    }
}


class House extends Observable {
    private double price;

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        super.setChanged();
        super.notifyObservers(price);
        this.price = price;
    }

}


class HouseHolder implements Observer{
    private String name;

    public HouseHolder(String name){
        this.name = name;
    }

    @Override
    public void update(Observable o, Object arg) {
        if(arg instanceof Double){
            System.out.println(this.name + " is noticed price change: " + (double)arg);
        }
    }
}