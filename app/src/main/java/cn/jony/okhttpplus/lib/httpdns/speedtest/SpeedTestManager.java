package cn.jony.okhttpplus.lib.httpdns.speedtest;

import cn.jony.okhttpplus.lib.httpdns.speedtest.impl.Socket80Test;

/**
 * 测速类
 * <p>
 * Created by fenglei on 15/4/22.
 */
public class SpeedTestManager implements ISpeedtest {

    /**
     * 请求server过程中发生错误
     */
    public static final int REQUEST_ERROR = -1;

    public static final int MAX_OVERTIME_RTT = 200;

    /**
     * 测速的轮询间隔
     */
    private BaseSpeedTest mSpeedTests = new Socket80Test();

    public SpeedTestManager() {
    }

    /**
     * @param host
     * @return
     */
    @Override
    public int speedTest(String ip, String host) {
        return mSpeedTests.speedTest(ip, host);
    }
}
