package com.ai.x;

import javax.sql.DataSource;

public class Main {
    public static void main(String[] args) {
//        TestClassLoader tcl = new TestClassLoader();
//        tcl.showClassloader();

//        writeDevice(60);
//        System.out.println("===============\n");
        writeDevice(1200);
    }


    public static void writeDevice(int paramInt) {
            byte[] arrayOfByte = new byte[10];
            boolean bool = false;
            byte b = 1;

            arrayOfByte[0] = (byte) -127;
            arrayOfByte[1] = (byte) 0;
            arrayOfByte[2] = (byte) 4;
            arrayOfByte[3] = (byte) 18;
            arrayOfByte[4] = (byte) 1;
            arrayOfByte[5] = (byte) (byte) (paramInt & 0xFF);
            arrayOfByte[6] = (byte) (byte) (paramInt / 256 & 0xFF);

            paramInt = bool ? 1 : 0;
            while (b <= 6) {
                paramInt += arrayOfByte[b] & 0xFF;
                b++;
            }
            StringBuilder stringBuilder2 = new StringBuilder();
            stringBuilder2.append("writeDevice: sum=");
            stringBuilder2.append(paramInt);
            System.out.println(stringBuilder2.toString());

            arrayOfByte[7] = (byte) (byte) (paramInt & 0xFF);
            arrayOfByte[8] = (byte) (byte) (paramInt / 256 & 0xFF);
            arrayOfByte[9] = (byte) -6;

            StringBuilder stringBuilder1 = new StringBuilder();
            stringBuilder1.append("writeDevice: writeByte=");
            stringBuilder1.append(ByteUtil.bytes2HexStr(arrayOfByte));
            System.out.println(stringBuilder1.toString());
    }

    public static void   test(){
        Animal animal = new Dog();

        if (animal instanceof Dog dog) {
            System.out.println(dog);
            dog.finalMethod();
        }

        if (animal instanceof Cat) {
            System.out.println("animal 是 Cat 类的实例");
        } else {
            System.out.println("animal 不是 Cat 类的实例");
        }


        String name = "setXXX";
        if (name.length() >= 4 && name.startsWith("set")) {
            String prop = Character.toLowerCase(name.charAt(3)) + name.substring(4);
            System.out.printf("Add row mapping: {} to {}({})", prop, name, "int");
        }
    }
}

class TestClassLoader{
    public void showClassloader(){
        ClassLoader cl1 = String.class.getClassLoader();
        ClassLoader cl2 = DataSource.class.getClassLoader();
        ClassLoader cl3 = Main.class.getClassLoader();


        System.out.println(cl1); // null
        System.out.println(cl2); // PlatformClassLoader
        System.out.println(cl3); // AppClassLoader
    }

}