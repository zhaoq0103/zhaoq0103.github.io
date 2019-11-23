package io.zq.basicclass.demo;

import java.io.*;
import java.util.function.IntUnaryOperator;
import java.util.zip.CRC32;
import java.util.zip.CheckedOutputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public class ZipDemo {
    public static void main(String[] args){
//        ZipFile zf = new ZipFile();
//        zf.run();

        ZipDir zd = new ZipDir();
        String filePath = "/Users/zhaoq0103/test.txt";
        zd.run(filePath);

/**
 * 可以正常使用
 */
//        try {
//            ZipUtil.compress("/Users/zhaoq0103/test");
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
    }
}

class ZipUtil {
    private static final String TAG = Class.class.getSimpleName();

    public static void compress(String srcPath) throws Exception{
            compress(srcPath, null);
    }

    public static void compress(String srcPath, String destPath ) throws Exception {

        //参数检查
        if (srcPath == null || srcPath.trim().length() == 0) {
            throw new IllegalArgumentException("srcPath or destPath is illegal");
        }

        if(destPath == null || destPath.trim().length() == 0)
            destPath = srcPath + ".zip";

        File srcFile = new File(srcPath);
        if (!srcFile.exists()) {
            throw new FileNotFoundException("srcPath file is not exists");
        }

        File destFile = new File(destPath);
        if (destFile.exists()) {
            if (!destFile.delete()) {
                throw new IllegalArgumentException("destFile is exist and do not delete.");
            }
        }

        CheckedOutputStream cos = null;
        ZipOutputStream zos = null;
        try {
            // 对目标文件做CRC32校验，确保压缩后的zip包含CRC32值
            cos = new CheckedOutputStream(new FileOutputStream(destPath), new CRC32());
            //装饰一层ZipOutputStream，使用zos写入的数据就会被压缩啦
            zos = new ZipOutputStream(cos);
            zos.setLevel(9);//设置压缩级别 0-9,0表示不压缩，1表示压缩速度最快，9表示压缩后文件最小；默认为6，速率和空间上得到平衡。
            if (srcFile.isFile()) {
                compressFile("", srcFile, zos);
            } else if (srcFile.isDirectory()) {
                compressFolder("", srcFile, zos);
            }
        } finally {
            closeQuietly(zos);
        }
    }

    private static void compressFolder(String prefix, File srcFolder, ZipOutputStream zos) throws IOException {
        String new_prefix = prefix + srcFolder.getName() + File.separator;
        File[] files = srcFolder.listFiles();
        //支持空文件夹
        if (files.length == 0) {
            compressFile(prefix, srcFolder, zos);
        } else {
            for (File file : files) {
                if (file.isFile()) {
                    compressFile(new_prefix, file, zos);
                } else if (file.isDirectory()) {
                    compressFolder(new_prefix, file, zos);
                }
            }
        }
    }

    private static void compressFile(String prefix, File src, ZipOutputStream zos) throws IOException {
        //若是文件,那肯定是对单个文件压缩
        //ZipOutputStream在写入流之前，需要设置一个zipEntry
        //注意这里传入参数为文件在zip压缩包中的路径，所以只需要传入文件名即可
        String relativePath = prefix + src.getName();
        if (src.isDirectory()) {//empty dir
            relativePath += "/";
        }
        ZipEntry entry = new ZipEntry(relativePath);
        //写到这个zipEntry中，可以理解为一个压缩文件
        zos.putNextEntry(entry);
        InputStream is = null;
        try {
            if (src.isFile()) {
                is = new FileInputStream(src);
                byte[] buffer = new byte[1024 << 3];
                int len = 0;
                while ((len = is.read(buffer)) != -1) {
                    zos.write(buffer, 0, len);
                }
            }
            //该文件写入结束
            zos.closeEntry();
        } finally {
            closeQuietly(is);
        }
    }

    private static void closeQuietly(final Closeable closeable) {
        try {
            if (closeable != null) {
                closeable.close();
            }
        } catch (final IOException ioe) {
        }
    }
}


class ZipDir{
    public void run(String filePath){
        File f = new File(filePath);
        File zipF = new File(filePath + ".zip");
        ZipOutputStream zos = null;
        try {
            zos = new ZipOutputStream(new FileOutputStream(zipF));
            zos.setComment("压缩文件");
            if(f.isFile()){
               zipFile("", f, zos);
            }else if(f.isDirectory()){
               zipDirs("", f, zos);
            }
            zos.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void zipDirs(String prefix, File f, ZipOutputStream zos) {
        String newPrefix = prefix + f.getName() + File.separator;

        if(f.isDirectory()){
            File[] files = f.listFiles();
            if(files.length == 0){//empty dir
                zipFile(prefix, f, zos);
            }else{
                for (int i = 0; i < files.length; i++) {
                    if(files[i].isDirectory()){
                        zipDirs(newPrefix,files[i], zos);
                    }else {
                        zipFile(newPrefix, files[i], zos);
                    }
                }
            }

        }
    }

    private void zipFile(String prefix,File f, ZipOutputStream zos){
        try {
            String relatePath = prefix + f.getName();
            if(f.isDirectory())
                relatePath += File.separator;

            zos.putNextEntry(new ZipEntry(relatePath));
            InputStream is = null;
            if(f.isFile()){
                is = new FileInputStream(f);
                int iTemp = 0;
                while ((iTemp = is.read()) != -1){
                    zos.write(iTemp);
                }
            }

            if(is != null) is.close();
            zos.closeEntry();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

//class ZipFile{
//    public void run(){
//        String filePath = "/Users/zhaoq0103/testfile.txt";
//        File f = new File(filePath);
//        File zipF = new File(filePath + ".zip");
//        try {
//            ZipOutputStream zos = new ZipOutputStream(new FileOutputStream(zipF));
//            zos.setComment("压缩文件");
//            zos.putNextEntry(new ZipEntry(f.getName()));
//            InputStream is = new FileInputStream(f);
//            int iTemp = 0;
//            while ((iTemp = is.read()) != -1){
//                zos.write(iTemp);
//            }
//            is.close();
//            zos.close();
//        } catch (FileNotFoundException e) {
//            e.printStackTrace();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }
//}
