package cn.liuyin.manhua.data.api;


import java.util.Map;
import java.util.TreeMap;

import cn.liuyin.manhua.tool.ComonTool;
import okhttp3.FormBody;

public class APIheper {
    /**
     * 获得签名
     *
     * @param localObject2
     * @return
     */
    private static String getSign(Object localObject2) {
        localObject2 = localObject2.toString();
        return ComonTool.getMD5String(ComonTool.getMD5String(ComonTool.getURLDecoderString(((String) localObject2).replaceAll(",", "&").replace("{", "").replace("}", "").replaceAll(" ", "") + "&key=PKwUJyO1GGraH7mDhClqWHExSPgGgcq")));
    }


    /**
     * 追加必要参数，获得请求
     *
     * @param builder
     * @return
     */
    public static FormBody.Builder getFormBuider(FormBody.Builder builder) {
        Object localObject2 = new TreeMap();
        builder.add("uid", "0");
        builder.add("channel_id", "6192");
        builder.add("app_version", "1.0.0");
        builder.add("timestamp", System.currentTimeMillis() + "");
        builder.add("platform", "Android");
        builder.add("device_id", ComonTool.getMD5String("Android"));
        builder.add("token", "");
        int i = 0;
        while (i < builder.build().size()) {
            ((Map) localObject2).put(builder.build().encodedName(i).toLowerCase(), builder.build().encodedValue(i));
            i += 1;
        }
        builder.add("sign", getSign(localObject2));
        return builder;
    }
}
