package cn.liuyin.manhua.data.api;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.Map;
import java.util.TreeMap;

import cn.liuyin.manhua.tool.ComonTool;
import okhttp3.FormBody;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;
import okio.Buffer;

public class ApiInterceptor implements Interceptor {
    public ApiInterceptor() {
    }

    @NotNull
    @Override
    public Response intercept(@NotNull Chain chain) throws IOException {
        Request original = chain.request();
        Request.Builder requestBuilder = original.newBuilder().addHeader("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");

        if (original.body() instanceof FormBody) {
            FormBody.Builder newFormBody = new FormBody.Builder();
            Map<String,String> stmap=new TreeMap<>();
            stmap.put("uid","0");
            stmap.put("channel_id","6192");
            stmap.put("app_version","3.3.3");
            stmap.put("timestamp",System.currentTimeMillis() + "");
            stmap.put("device_id",ComonTool.getMD5String("Android" + System.currentTimeMillis()));
            stmap.put("token","");
            Map<String, String> map = new TreeMap<>();
            FormBody oldFormBody = (FormBody) original.body();

            for (int i = 0; i < oldFormBody.size(); i++) {
                String key = oldFormBody.encodedName(i).toLowerCase();
                String value = oldFormBody.encodedValue(i);
                map.put(key, value);
                newFormBody.addEncoded(key, value);
            }
            for (Map.Entry<String, String> stringStringEntry : stmap.entrySet()) {
                String key = (String) stringStringEntry.getKey();
                String value = (String) stringStringEntry.getValue();
                map.put(key, value);
                newFormBody.addEncoded(key, value);
            }
            newFormBody.add("sign", ComonTool.getMD5String(ComonTool.getMD5String(ComonTool.getURLDecoderString(map.toString().replaceAll(",", "&").replace("{", "").replace("}", "").replaceAll(" ", "") + "&key=PKwUJyO1GGraH7mDhClqWHExSPgGgcq"))));
            requestBuilder.method(original.method(), newFormBody.build());
        } else {
            original.body().writeTo(new Buffer());
        }
        return chain.proceed(requestBuilder.build());
    }
}
