package cn.liuyin.manhua.tool;


import android.content.Context;
import android.content.Intent;
import android.net.Uri;

import java.io.File;

public class ShareUtils {
    public static void shareString(Context context, String content) {
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("text/plain");
        intent.putExtra(Intent.EXTRA_SUBJECT, "标题");
        intent.putExtra(Intent.EXTRA_TEXT, content);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(Intent.createChooser(intent, "分享到"));
    }

    public static void shareImageWithTitle(Context context, String filename, String title) {
        String path = FileTool.BASEPATH + "/img";
        File file = new File(path, filename);//这里share.jpg是sd卡根目录下的一个图片文件
        Uri imageUri = Uri.fromFile(file);
        Intent shareIntent = new Intent();
        shareIntent.setAction(Intent.ACTION_SEND);
        shareIntent.putExtra(Intent.EXTRA_TEXT, title);
        shareIntent.putExtra(Intent.EXTRA_STREAM, imageUri);
        shareIntent.setType("image/*");
        context.startActivity(Intent.createChooser(shareIntent, "分享图片"));
    }

    public static void shareImage(Context context, String filename) {
        String path = FileTool.BASEPATH + "/img";
        File file = new File(path, filename);//这里share.jpg是sd卡根目录下的一个图片文件
        Uri imageUri = Uri.fromFile(file);
        Intent shareIntent = new Intent();
        shareIntent.setAction(Intent.ACTION_SEND);
        shareIntent.putExtra(Intent.EXTRA_TEXT, "测试");
        shareIntent.putExtra(Intent.EXTRA_STREAM, imageUri);
        shareIntent.setType("image/*");
        context.startActivity(Intent.createChooser(shareIntent, "分享图片"));
    }

    public static void shareImage(Context context, String filename, String classname) {
        String path = FileTool.BASEPATH + "/img";
        File file = new File(path, "share" + ".jpg");//这里share.jpg是sd卡根目录下的一个图片文件
        Uri imageUri = Uri.fromFile(file);
        Intent shareIntent = new Intent();
        shareIntent.setAction(Intent.ACTION_SEND);
        shareIntent.putExtra(Intent.EXTRA_TEXT, "测试");
        shareIntent.putExtra(Intent.EXTRA_STREAM, imageUri);
        shareIntent.setType("image/*");
        context.startActivity(Intent.createChooser(shareIntent, "分享图片"));
    }
}
