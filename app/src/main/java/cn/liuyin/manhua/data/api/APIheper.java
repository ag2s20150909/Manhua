package cn.liuyin.manhua.data.api;


import android.content.Context;

import java.util.TreeMap;

import cn.liuyin.manhua.APP;
import cn.liuyin.manhua.tool.ComonTool;
import okhttp3.FormBody;

class APIheper {
    /**
     * 获得签名
     *
     * @param map
     * @return sign
     */
    private static String getSign(TreeMap<String, String> map) {
        String str = map.toString();
        return ComonTool.getMD5String(ComonTool.getMD5String(ComonTool.getURLDecoderString(str.replaceAll(",", "&").replace("{", "").replace("}", "").replaceAll(" ", "") + "&key=PKwUJyO1GGraH7mDhClqWHExSPgGgcq")));
    }


    /**
     * 追加必要参数，获得请求
     *
     * @param builder
     * @return builder
     */
    public static FormBody.Builder getFormBuider(FormBody.Builder builder) {
        TreeMap<String, String> map = new TreeMap<>();
        builder.add("uid", "0");
        String version = APP.getContext().getSharedPreferences("api", Context.MODE_PRIVATE).getString("version", "3.1.5");
        builder.add("channel_id", "6192");
        //System.err.println("APP_DEBUG:" + version);
        builder.add("app_version", version);
        builder.add("timestamp", System.currentTimeMillis() + "");
        builder.add("pvdevice_id", ComonTool.getMD5String("Android" + System.currentTimeMillis()));
        builder.add("token", "");
        int i = 0;

        while (i < builder.build().size()) {
            map.put(builder.build().encodedName(i).toLowerCase(), builder.build().encodedValue(i));
            i += 1;
        }
        builder.add("sign", getSign(map));
        return builder;
    }
}
