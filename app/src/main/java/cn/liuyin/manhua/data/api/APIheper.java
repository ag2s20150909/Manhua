package cn.liuyin.manhua.data.api;


import java.util.Map;
import java.util.TreeMap;

import cn.liuyin.manhua.tool.ComonTool;
import okhttp3.FormBody;

public class APIheper {
    public static String getSign(Object localObject2) {
        localObject2 = localObject2.toString();
        String sign = ComonTool.getMD5String(ComonTool.getMD5String(ComonTool.getURLDecoderString(((String) localObject2).replaceAll(",", "&").replace("{", "").replace("}", "").replaceAll(" ", "") + "&key=PKwUJyO1GGraH7mDhClqWHExSPgGgcq")));
        return sign;
    }


    public static FormBody.Builder getFormBuider(FormBody.Builder builder) {
        Object localObject2 = new TreeMap();
        FormBody.Builder params = builder;
        params.add("uid", "0");
        params.add("channel_id", "6192");
        params.add("app_version", "1.0.0");
        params.add("timestamp", System.currentTimeMillis() + "");
        params.add("platform", "Android");
        params.add("device_id", ComonTool.getMD5String("Android"));
        params.add("token", "");
        int i = 0;
        while (i < params.build().size()) {
            ((Map) localObject2).put(params.build().encodedName(i).toLowerCase(), params.build().encodedValue(i));
            i += 1;
        }
        params.add("sign", getSign(localObject2));
        return params;
    }
}
