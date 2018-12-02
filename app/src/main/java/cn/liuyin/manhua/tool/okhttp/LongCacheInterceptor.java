package cn.liuyin.manhua.tool.okhttp;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.CacheControl;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

public class LongCacheInterceptor implements Interceptor {


    @Override
    public Response intercept(Chain chain) throws IOException {
        CacheControl FORCE_CACHE = new CacheControl.Builder()
                .onlyIfCached()
                .maxStale(Integer.MAX_VALUE, TimeUnit.SECONDS)
                .build();
        Request originalRequest = chain.request();
        Request compressedRequest = originalRequest.newBuilder().cacheControl(FORCE_CACHE).build();
        return chain.proceed(compressedRequest);

    }


}