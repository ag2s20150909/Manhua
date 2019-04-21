package cn.jony.okhttpplus.lib.httpdns.speedtest.impl;

import android.text.TextUtils;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

import cn.jony.okhttpplus.lib.httpdns.speedtest.BaseSpeedTest;
import cn.jony.okhttpplus.lib.httpdns.speedtest.SpeedTestManager;

public class PingTest extends BaseSpeedTest {

    @Override
    public int speedTest(String ip, String host) {
        try {
            return Ping.runcmd("ping -c1 -s1 -w1 " + ip);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return SpeedTestManager.REQUEST_ERROR;
    }

    @Override
    public int getPriority() {
        return 1;
    }

    @Override
    public boolean isActivate() {
        return false;
    }

    public static class Ping {
        // ping -c1 -s1 -w1 www.baidu.com //-w 超时单位是s
        private static final String TAG_BYTES_FROM = "bytes from ";

        public static int runcmd(String cmd) throws Exception {
            Runtime runtime = Runtime.getRuntime();
            Process proc = null;

            final String command = cmd.trim();
            long startTime = System.currentTimeMillis();
            proc = runtime.exec(command);
            proc.waitFor();
            long endTime = System.currentTimeMillis();
            InputStream inputStream = proc.getInputStream();
            String result;

            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
            StringBuilder resultBuilder = new StringBuilder();
            String line;
            while (null != (line = reader.readLine())) {
                resultBuilder.append(line);
            }
            reader.close();
            String responseStr = resultBuilder.toString();
            result = responseStr.toLowerCase().trim();
            if (isValidResult(result)) {
                return (int) (endTime - startTime);
            }
            return SpeedTestManager.REQUEST_ERROR;
        }

        private static boolean isValidResult(String result) {
            if (!TextUtils.isEmpty(result)) {
                return result.indexOf(TAG_BYTES_FROM) > 0;
            }
            return false;
        }
    }
}
