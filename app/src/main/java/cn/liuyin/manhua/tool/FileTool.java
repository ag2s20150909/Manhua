package cn.liuyin.manhua.tool;


import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class FileTool {
    static final String BASEPATH = "/sdcard/ag2sapp/manhua";

    //删除文件夹和文件夹里面的文件
    public static void deleteDir(final String pPath) {
        File dir = new File(BASEPATH, pPath);
        deleteDirWihtFile(dir);
    }

    public static void deleteDirWihtFile(File dir) {
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
        if (f.exists()) {
            return true;
        } else {
            return false;
        }
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
            String s = "";
            BufferedReader br = new BufferedReader(new FileReader(file));

            while ((s = br.readLine()) != null) {
                sb.append(s + "\n");
            }

            br.close();
            return sb.toString();
        } catch (IOException e) {
            FileTool.writeError(e.getMessage());
            return e.getMessage();
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
