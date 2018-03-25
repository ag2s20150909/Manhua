package cn.liuyin.manhua.tool;


import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.io.StringWriter;

import cn.liuyin.manhua.APP;

public class FileTool {

    public static final String BASEPATH = Environment.getExternalStorageDirectory().getPath() + "/ag2sapp/manhua";

    public static void saveBitmap(Context context, String fileName, Bitmap bmp) {
        // 首先保存图片
        File appDir = new File(BASEPATH, "img");
        if (!appDir.exists()) {
            appDir.mkdir();
        }
        //String fileName = System.currentTimeMillis() + ".jpg";
        File file = new File(appDir, fileName);
        try {
            FileOutputStream fos = new FileOutputStream(file);
            bmp.compress(Bitmap.CompressFormat.JPEG, 100, fos);
            fos.flush();
            fos.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        // 其次把文件插入到系统图库
        try {
            MediaStore.Images.Media.insertImage(context.getContentResolver(),
                    file.getAbsolutePath(), fileName, null);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        // 最后通知图库更新
        context.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.parse("file://" + file.getAbsolutePath())));
    }

    //删除文件夹和文件夹里面的文件
    public static void deleteDir(final String pPath) {
        File dir = new File(BASEPATH, pPath);
        deleteDirWihtFile(dir);
    }

    private static void deleteDirWihtFile(File dir) {
        if (dir == null || !dir.exists() || !dir.isDirectory())
            return;
        for (File file : dir.listFiles()) {
            if (file.isFile())
                file.delete(); // 删除所有文件
            else if (file.isDirectory())
                deleteDirWihtFile(file); // 递规的方式删除文件夹
        }
        dir.delete();// 删除目录本身
    }

    public static boolean has(String name) {
        File f = new File(BASEPATH, name);
        return f.exists();
    }

    public static boolean has(String base, String name) {
        String path = BASEPATH + "/" + base;
        File f = new File(path, name);
        return f.exists();
    }

    public static String readFile(String base, String filename) {
        String path = BASEPATH + "/" + base;
        try {
            File f = new File(BASEPATH);
            if (!f.exists()) {
                f.mkdirs();
            }
            File file = new File(path, filename);
            File file1 = new File(path);
            if (!file1.exists()) {
                file1.mkdirs();
            }
            StringBuilder sb = new StringBuilder();
            String s;
            BufferedReader br = new BufferedReader(new FileReader(file));

            while ((s = br.readLine()) != null) {
                sb.append(s).append("\n");
            }

            br.close();
            return sb.toString();
        } catch (IOException e) {
            FileTool.writeError(e.getMessage());
            return e.getMessage();
        }
    }

    public static String readAsset(String fileName) {
        try {
            InputStream is = APP.getContext().getAssets().open(fileName);
            int lenght = is.available();
            byte[] buffer = new byte[lenght];
            is.read(buffer);
            String result = new String(buffer, "utf8");
            return result;
        } catch (Exception e) {
            return null;
        }
    }

    public static void writeFiles(String name, String filename, String content) {
        String path = BASEPATH + "/" + name;
        try {
            File f = new File(BASEPATH);
            if (!f.exists()) {
                f.mkdirs();
            }
            File file1 = new File(path);
            if (!file1.exists()) {
                file1.mkdirs();
            }
            File file = new File(path, filename);
            if (!file.exists()) {
                file.createNewFile();
            }
            FileWriter fw = new FileWriter(file);
            fw.write(content);
            fw.flush();
            fw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void combine(String name, String filename, String content) {
        String path = BASEPATH + "/" + name;
        try {
            File f = new File(BASEPATH);
            if (!f.exists()) {
                f.mkdirs();
            }
            File file = new File(path, filename);
            if (!file.exists()) {
                file.createNewFile();
            }
            FileWriter fw = new FileWriter(file, true);
            fw.write(readFile(name, content));
            fw.flush();
            fw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void writeFile(String filename, String content) {
        String path = BASEPATH;
        try {
            File f = new File(BASEPATH);
            if (!f.exists()) {
                f.mkdirs();
            }
            File file = new File(path, filename);
            if (!file.exists()) {
                file.createNewFile();
            }
            FileWriter fw = new FileWriter(file);
            fw.write(content);
            fw.flush();
            fw.close();
        } catch (IOException e) {
            //Toast.makeText(APP.getContext(),e.getMessage(),1).show();
        }
    }

    public static void writeError(Exception e) {

        writeError(getStackTrace(e));
    }

    public static String getStackTrace(Throwable throwable) {
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);

        try {
            throwable.printStackTrace(pw);
            return sw.toString();
        } finally {
            pw.close();
        }
    }

    public static void writeError(String content) {
        String path = BASEPATH;
        try {
            File f = new File(BASEPATH);
            if (!f.exists()) {
                f.mkdirs();
            }
            File file = new File(path, "error.txt");
            if (!file.exists()) {
                file.createNewFile();
            }
            FileWriter fw = new FileWriter(file, true);
            fw.write(content + "\n");
            fw.flush();
            fw.close();
        } catch (IOException e) {
            //Toast.makeText(APP.getContext(),e.getMessage(),1).show();
        }
    }
}
