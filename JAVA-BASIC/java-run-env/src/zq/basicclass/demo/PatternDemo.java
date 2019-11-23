package zq.basicclass.demo;

public class PatternDemo {
    public static void main(String[] args){
       StringPattenDemo spd = new StringPattenDemo();
       spd.print();
    }
}


class StringPattenDemo{
    String info = "A1B22C333D4444E55555F".replaceAll("\\d+", "-");
    String[] tmp = "A1B22C333D4444E55555F".split("\\d+");

    public void print(){
//        String info = "A1B22C333D4444E55555F".replaceAll("\\d+", "-");
//        String[] tmp = "A1B22C333D4444E55555F".split("\\d+");

        System.out.println("info:" + info);
        for (int i = 0; i < tmp.length; i++) {
            System.out.print(tmp[i] + "\t");
        }
        System.out.println();
    }
}
