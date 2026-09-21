package com.ai.x;

//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//


import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;

public class ByteUtil {
    private static String TAG;
    public int[] crctab = new int[]{0, 32773, 32783, 10, 32795, 30, 20, 32785, 32819, 54, 60, 32825, 40, 32813, 32807, 34, 32867, 102, 108, 32873, 120, 32893, 32887, 114, 80, 32853, 32863, 90, 32843, 78, 68, 32833, 32963, 198, 204, 32969, 216, 32989, 32983, 210, 240, 33013, 33023, 250, 33003, 238, 228, 32993, 160, 32933, 32943, 170, 32955, 190, 180, 32945, 32915, 150, 156, 32921, 136, 32909, 32903, 130, 33155, 390, 396, 33161, 408, 33181, 33175, 402, 432, 33205, 33215, 442, 33195, 430, 420, 33185, 480, 33253, 33263, 490, 33275, 510, 500, 33265, 33235, 470, 476, 33241, 456, 33229, 33223, 450, 320, 33093, 33103, 330, 33115, 350, 340, 33105, 33139, 374, 380, 33145, 360, 33133, 33127, 354, 33059, 294, 300, 33065, 312, 33085, 33079, 306, 272, 33045, 33055, 282, 33035, 270, 260, 33025, 33539, 774, 780, 33545, 792, 33565, 33559, 786, 816, 33589, 33599, 826, 33579, 814, 804, 33569, 864, 33637, 33647, 874, 33659, 894, 884, 33649, 33619, 854, 860, 33625, 840, 33613, 33607, 834, 960, 33733, 33743, 970, 33755, 990, 980, 33745, 33779, 1014, 1020, 33785, 1000, 33773, 33767, 994, 33699, 934, 940, 33705, 952, 33725, 33719, 946, 912, 33685, 33695, 922, 33675, 910, 900, 33665, 640, 33413, 33423, 650, 33435, 670, 660, 33425, 33459, 694, 700, 33465, 680, 33453, 33447, 674, 33507, 742, 748, 33513, 760, 33533, 33527, 754, 720, 33493, 33503, 730, 33483, 718, 708, 33473, 33347, 582, 588, 33353, 600, 33373, 33367, 594, 624, 33397, 33407, 634, 33387, 622, 612, 33377, 544, 33317, 33327, 554, 33339, 574, 564, 33329, 33299, 534, 540, 33305, 520, 33293, 33287, 514};

    public ByteUtil() {
    }

    public static String bytes2HexStr(byte[] var0) {
        StringBuilder var1 = new StringBuilder();
        if (var0 != null && var0.length > 0) {
            char[] var2 = new char[2];
            int var3 = var0.length;

            for(int var4 = 0; var4 < var3; ++var4) {
                byte var5 = var0[var4];
                var2[0] = Character.forDigit(var5 >>> 4 & 15, 16);
                var2[1] = Character.forDigit(var5 & 15, 16);
                var1.append(var2);
            }

            return var1.toString().toUpperCase();
        } else {
            return "";
        }
    }

    public static String bytes2HexStr(byte[] var0, int var1, int var2) {
        byte[] var3 = new byte[var2];
        System.arraycopy(var0, var1, var3, 0, var2);
        return bytes2HexStr(var3);
    }

    public static short calc_crc(short[] var0) {
        int var1 = var0.length;
        short var8 = 0;
        short var2 = 0;

        short var3;
        int var5;
        for(var3 = var2; var8 < var0.length; var3 = (short)var5) {
            short var4 = (short)(var0[var8] << 8);
            var2 = 0;

            for(var5 = var3; var2 < 8; ++var2) {
                if ((var5 ^ var4) < 0) {
                    var5 = var5 << 1 ^ 4129;
                } else {
                    var5 <<= 1;
                }

                var5 = (short)var5;
                var4 = (short)(var4 << 1);
            }

            ++var8;
        }

        PrintStream var7 = System.err;
        StringBuilder var6 = new StringBuilder();
        var6.append("+++  crc ++++");
        var6.append(Integer.toHexString(var3));
        var7.println(var6.toString());
        return var3;
    }

    public static String check(List<String> var0) {
        int var1 = 0;

        int var2;
        for(var2 = 0; var1 < var0.size(); ++var1) {
            String var3 = (String)var0.get(var1);
            var2 = (int)((long)var2 + hexStr2decimal(var3));
        }

        StringBuilder var4 = new StringBuilder(decimal2fitHex((long)var2));

        while(var4.length() < 4) {
            var4.insert(0, "0");
        }

        return var4.toString();
    }

    public static String decimal2fitHex(long var0) {
        String var2 = Long.toHexString(var0).toUpperCase();
        if (var2.length() % 2 != 0) {
            StringBuilder var3 = new StringBuilder();
            var3.append("0");
            var3.append(var2);
            return var3.toString();
        } else {
            return var2.toUpperCase();
        }
    }

    public static String decimal2fitHex(long var0, int var2) {
        StringBuilder var3 = new StringBuilder(decimal2fitHex(var0));

        while(var3.length() < var2) {
            var3.insert(0, '0');
        }

        return var3.toString();
    }

    private static String endData(List<String> var0) {
        StringBuilder var1 = new StringBuilder("81");
        ArrayList var2 = new ArrayList();
        var2.add("01");
        var2.add(decimal2fitHex((long)var0.size()));
        var2.addAll(var0);

        for(int var3 = 0; var3 < var2.size(); ++var3) {
            var1.append((String)var2.get(var3));
        }

        String var5 = check(var2);
        String var4 = var5.substring(0, 2);
        var1.append(var5.substring(2, 4));
        var1.append(var4);
        var1.append("FA");
        return var1.toString();
    }

    public static String fitDecimalStr(int var0, int var1) {
        StringBuilder var2 = new StringBuilder(String.valueOf(var0));

        while(var2.length() < var1) {
            var2.insert(0, "0");
        }

        return var2.toString();
    }

    private static int hexChar2byte(char var0) {
        switch (var0) {
            case '0':
                return 0;
            case '1':
                return 1;
            case '2':
                return 2;
            case '3':
                return 3;
            case '4':
                return 4;
            case '5':
                return 5;
            case '6':
                return 6;
            case '7':
                return 7;
            case '8':
                return 8;
            case '9':
                return 9;
            default:
                switch (var0) {
                    case 'B':
                        return 11;
                    case 'C':
                        return 12;
                    case 'D':
                        return 13;
                    case 'E':
                        return 14;
                    case 'F':
                        return 15;
                    default:
                        switch (var0) {
                            case 'a':
                                break;
                            case 'b':
                                return 11;
                            case 'c':
                                return 12;
                            case 'd':
                                return 13;
                            case 'e':
                                return 14;
                            case 'f':
                                return 15;
                            default:
                                return -1;
                        }
                    case 'A':
                        return 10;
                }
        }
    }

    public static byte[] hexStr2bytes(String var0) {
        int var1 = var0.length() / 2;
        byte[] var2 = new byte[var1];
        char[] var6 = var0.toUpperCase().toCharArray();

        for(int var3 = 0; var3 < var1; ++var3) {
            int var4 = var3 * 2;
            int var5 = hexChar2byte(var6[var4]);
            var2[var3] = (byte)((byte)((hexChar2byte(var6[var4 + 1]) | var5 << 4) & 255));
        }

        return var2;
    }

    public static long hexStr2decimal(String var0) {
        return Long.parseLong(var0, 16);
    }

    public static short[] hexStr2short(String var0) {
        int var1 = var0.length() / 2;
        short[] var2 = new short[var1];
        char[] var6 = var0.toUpperCase().toCharArray();

        for(int var3 = 0; var3 < var1; ++var3) {
            int var4 = var3 * 2;
            int var5 = hexChar2byte(var6[var4]);
            var2[var3] = (short)((short)((hexChar2byte(var6[var4 + 1]) | var5 << 4) & 255));
        }

        return var2;
    }

    public static String str2HexString(String var0) {
        char[] var1 = "0123456789ABCDEF".toCharArray();
        StringBuilder var2 = new StringBuilder();

        byte[] var5;
        try {
            var5 = var0.getBytes("utf8");
        } catch (Exception var4) {
            var4.printStackTrace();
            var5 = null;
        }

        for(int var3 = 0; var3 < var5.length; ++var3) {
            var2.append(var1[(var5[var3] & 240) >> 4]);
            var2.append(var1[var5[var3] & 15]);
        }

        return var2.toString();
    }

    public void CrcAdd(char[] var1, int var2) {
        int var3 = this.CrcCalc(var1, var2);
        var1[var2] = (char)((char)(var3 >> 8));
        var1[var2 + 1] = (char)((char)var3);
    }

    public int CrcCalc(char[] var1, int var2) {
        int var3 = 0;

        int var4;
        for(var4 = 0; var3 < var2; ++var3) {
            char var5 = (char)((char)(var4 >> 8) ^ var1[var3]);
            var4 = (char)(var4 << 8) ^ this.crctab[var5];
        }

        return var4;
    }
}
