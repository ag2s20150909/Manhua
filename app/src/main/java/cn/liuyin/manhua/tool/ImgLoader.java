package cn.liuyin.manhua.tool;

import android.content.Context;
import android.os.Handler;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.io.File;

import cn.liuyin.manhua.APP;
import cn.liuyin.manhua.R;


public class ImgLoader {
    private static ImgLoader me;
    private Context context;
    private Handler handler;

    private ImgLoader() {
        context = APP.getContext();
    }

    public static ImgLoader getInstance() {
        if (me == null) {
            me = new ImgLoader();
        }
        return me;
    }

    public ImgLoader load(String url, final ImageView iv, String name) {

        File file = new File(FileTool.BASEPATH + "/img", name);
        if (file.exists()) {
            url = file.getAbsolutePath();
            Picasso.get().load("file://" + url).placeholder(R.drawable.ic_loading).into(iv);
        } else {
            Picasso.get().load(url).placeholder(R.drawable.ic_loading).into(iv);
        }


        return this;

    }


}

