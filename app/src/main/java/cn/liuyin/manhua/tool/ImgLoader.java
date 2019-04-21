package cn.liuyin.manhua.tool;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.widget.ImageView;

import java.io.File;

import cn.liuyin.manhua.R;

public class ImgLoader {
    private Handler handler;
    private static ImgLoader me;

    public static ImgLoader getInstance() {
        if (me == null) {
            me = new ImgLoader();
        }
        return me;
    }

    private ImgLoader() {
        handler = new Handler();
    }

    public ImgLoader load(final File file, final ImageView iv) {
        iv.setBackgroundResource(R.drawable.ic_loading);
        handler.postDelayed(new Runnable() {

            @Override
            public void run() {
                // TODO: Implement this method
                Bitmap bitmap = BitmapFactory.decodeFile(file.getAbsolutePath());
                iv.setImageBitmap(bitmap);
            }
        }, 100);
        return this;
    }
}


