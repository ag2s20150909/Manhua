package cn.liuyin.manhua.tool.okhttp;

import android.text.TextUtils;

import com.jakewharton.disklrucache.DiskLruCache;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import cn.liuyin.manhua.tool.FileTool;


public class DiskCache {
    private static final int MAX_SIZE = 10 * 1024 * 1024;//10M
    private static DiskCache mD;
    private DiskLruCache mDiskCache;

    private DiskCache() {
        try {
            File f = new File(FileTool.BASEPATH + "/httpdns");
            f.mkdirs();
            mDiskCache = DiskLruCache.open(f, 1, 1, MAX_SIZE);
//DiskLruCache mDiskCache = DiskLruCache.open(diskCacheFile, appVersion, valueCount, maxSize);
// diskCacheFile 是本地可写的文件目录
// valueCount 是表示一个key对应多少个文件，一般是1
// maxSize 最大容量
        } catch (IOException e) {
        }
    }

    public static DiskCache getInstance() {
        if (mD == null) {
            mD = new DiskCache();
        }
        return mD;
    }

    public static String md5(String string) {
        if (TextUtils.isEmpty(string)) {
            return "";
        }
        MessageDigest md5;
        try {
            md5 = MessageDigest.getInstance("MD5");
            byte[] bytes = md5.digest(string.getBytes());
            StringBuilder result = new StringBuilder();
            for (byte b : bytes) {
                String temp = Integer.toHexString(b & 0xff);
                if (temp.length() == 1) {
                    temp = "0" + temp;
                }
                result.append(temp);
            }
            return result.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return "";
    }

    public String getString(String key) {
        String data = null;
        try {
            // 读取缓存
            DiskLruCache.Snapshot snapshot = mDiskCache.get(md5(key));
            InputStream ins = snapshot.getInputStream(0);
            byte[] b1 = new byte[4];
            ins.read(b1);
            int old = ByteArrayToInt(b1);
            ins.read(b1);
            int time = ByteArrayToInt(b1);
            int now = (int) (System.currentTimeMillis() / 1000);
//			FileTool.writeLog("test.txt",""+old+"-"+time);
            if (now > (old + time)) {
                mDiskCache.remove(md5(key));
                return null;
            }
            byte[] buffer = new byte[2048];
            int readBytes;
            StringBuilder stringBuilder = new StringBuilder();
            while ((readBytes = ins.read(buffer)) > 0) {

                stringBuilder.append(new String(buffer, 0, readBytes));

            }

            // 0表示第一个缓存文件，不能超过valueCount
            return stringBuilder.toString();
        } catch (Exception e) {
            return null;
        }

    }

    public void saveString(String key, String data, int time) {
        try {
            int now = (int) (System.currentTimeMillis() / 1000);
            byte[] bt1 = IntToByteArray(now);
            byte[] bt2 = IntToByteArray(time);
            byte[] bt3 = data.getBytes();

            bt1 = add(bt1, bt2);
            bt1 = add(bt1, bt3);

            DiskLruCache.Editor editor = mDiskCache.edit(md5(key));
            OutputStream outputStream = editor.newOutputStream(0);// 0表示第一个缓存文件，不能超过valueCount
            outputStream.write(bt1);
            outputStream.close();
            editor.commit();
            mDiskCache.flush();
        } catch (Exception e) {
            FileTool.writeError(e);
        }
    }

    private byte[] add(byte[] old, byte[] add) {
        byte[] data = new byte[old.length + add.length];
        System.arraycopy(old, 0, data, 0, old.length);
        System.arraycopy(add, 0, data, old.length, add.length);
        return data;
    }

    public byte[] IntToByteArray(int n) {
        byte[] b = new byte[4];
        b[3] = (byte) (n & 0xff);
        b[2] = (byte) (n >> 8 & 0xff);
        b[1] = (byte) (n >> 16 & 0xff);
        b[0] = (byte) (n >> 24 & 0xff);
        return b;
    }

    //将低字节在前转为int，高字节在后的byte数组(与IntToByteArray1想对应)
    public int ByteArrayToInt(byte[] bArr) {
        if (bArr.length != 4) {
            return -1;
        }
        return (((bArr[0] & 0xff) << 24)
                | ((bArr[1] & 0xff) << 16)
                | ((bArr[2] & 0xff) << 8)
                | ((bArr[3] & 0xff)));
    }

    private String inputStream2String(InputStream is) throws IOException {
        BufferedReader in = new BufferedReader(new InputStreamReader(is));
        StringBuffer buffer = new StringBuffer();
        String line;
        while ((line = in.readLine()) != null) {
            buffer.append(line);
        }
        return buffer.toString();
    }
}
