package collection.zq.basicclass.demo;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Properties;

public class PropertiesDemo {
    public static void main(String[] args){
        ProperStore ps = new ProperStore();
        ps.run();

    }
}

class ProperStore{
    public void run(){
        Properties p = new Properties();
        p.setProperty("BJ", "beijing");
        p.setProperty("SZ", "shenzhen");
        p.setProperty("SH", "shanghai");

        File pFile = new File("/Users/zhaoq0103/area.properties");
        File pxmlFile = new File("/Users/zhaoq0103/area.xml");
        try {
            p.store(new FileWriter(pFile), "地区信息");
            p.storeToXML(new FileOutputStream(pxmlFile), "地区信息", "UTF-8");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
