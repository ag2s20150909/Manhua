package cn.liuyin.manhua.tool;


import java.io.Closeable;
import java.io.File;

import okio.BufferedSource;
import okio.Okio;
import okio.Source;

public class OkIoTool {
    public static void read() {
        Source source;
        BufferedSource buffersource;
        try {
            File f = new File("");
            source = Okio.source(f);
            buffersource = Okio.buffer(source);
            String msg = buffersource.readUtf8();

        } catch (Exception e) {

        }
    }

    public static void closeQuietly(Closeable closeable) {

        if (closeable != null) {

            try {

                closeable.close();

            } catch (RuntimeException rethrown) {

                throw rethrown;

            } catch (Exception ignored) {

            }

        }
    }
}
