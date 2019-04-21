package cn.jony.okhttpplus.lib.httpdns.net.networktype;

import android.annotation.SuppressLint;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.telephony.TelephonyManager;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.Collections;


@SuppressLint("NewApi")
public class NetworkManager extends Constants {

    private static NetworkManager Instance = null;
    private static Context sContext;
    /**
     * 当前网络类型
     */
    public int networkType = -1111;
    /**
     * 当前网络类型名字
     */
    public String networkTypeStr = "-1111";
    /**
     * 当前SP类型
     */
    public int operatorType = -1111;
    /**
     * 当前SP类型名字
     */
    public String operatorTypeStr = "-1111";
    /**
     * IP地址
     */
    public String ipAddress = "-1111";
    /**
     * MAC地址
     */
    public String macAddress = "-1111";

    public NetworkManager() {
    }

    public static NetworkManager CreateInstance(Context context) {

        setContext(context);

        if (Instance == null) {
            Instance = new NetworkManager();
            Instance.init();
        }
        return Instance;
    }

    public static NetworkManager getInstance() {
        if (Instance == null) {
            throw new IllegalStateException("must invoke CreateInstance first");
        }
        return Instance;
    }

    public static void setContext(Context ctx) {
        Context app = ctx.getApplicationContext();
        if (sContext == app) {
            return;
        }
        sContext = app;
    }

    /**
     * 初始化网络环境信息数据，可重复
     */
    public void init() {

        new Thread(new Runnable() {
            public void run() {
                Thread.currentThread().setName("Net Work Manager init");

                networkType = Util.getNetworkType();

                switch (networkType) {

                    case Constants.NETWORK_TYPE_UNCONNECTED:
                    case Constants.NETWORK_TYPE_UNKNOWN:
                        break;

                    case Constants.NETWORK_TYPE_WIFI:
                        ipAddress = Util.getLocalIpAddress();
                        macAddress = Util.getRouteMac();
                        operatorType = Util.getWifiOperator();
                        break;

                    case Constants.NETWORK_TYPE_2G:
                    case Constants.NETWORK_TYPE_3G:
                    case Constants.NETWORK_TYPE_4G:
                        operatorType = Util.getOperator();
                        break;
                }

                networkTypeStr = Constants.NetType2Str(networkType);

                if (networkType != NETWORK_TYPE_WIFI) {
                    operatorTypeStr = Constants.Operator2Str(operatorType);
                } else {
                    operatorTypeStr = Util.getWifiSSID(NetworkManager.sContext);
                }


            }
        }).start();

    }


    public String toString() {
        String str = "";

        str += "当前网络类型ID:" + networkType + "\n";
        str += "当前网络类型名字:" + networkTypeStr + "\n\n";

        str += "当前服务商类型ID:" + operatorType + "\n";
        str += "当前服务商类型名字:" + operatorTypeStr + "\n\n";

        str += "内网IP:" + ipAddress + "\n";
        str += "当前MAC:" + macAddress + "\n\n";

        return str;
    }


    public String getOperatorId() {
        if (networkType == NETWORK_TYPE_WIFI) {
            return operatorTypeStr;
        }
        return String.valueOf(operatorType);
    }

    public boolean isNetOK() {
        return Util.getNetworkType() != Constants.NETWORK_TYPE_UNCONNECTED &&
                Util.getNetworkType() != Constants.MOBILE_UNKNOWN;
    }

    public static class Util {

        /**
         * 获取本机IP函数
         */
        public static String getLocalIpAddress() {
            try {
                String ipv4;
                ArrayList<NetworkInterface> mylist = Collections.list(NetworkInterface
                        .getNetworkInterfaces());
                for (NetworkInterface ni : mylist) {
                    ArrayList<InetAddress> iaList = Collections.list(ni.getInetAddresses());
                    for (InetAddress address : iaList) {
                        if (!address.isLoopbackAddress() && InetAddressUtils.isIPv4Address(ipv4 =
                                address.getHostAddress())) {
                            return ipv4;
                        }
                    }
                }
            } catch (SocketException ex) {
                ex.printStackTrace();
            }
            return "0";
        }

        /**
         * 获取本机MAC地址
         */
        public static String getRouteMac() {
            try {
                WifiManager wifiManager = (WifiManager) sContext.getSystemService(Context
                        .WIFI_SERVICE);
                WifiInfo wifiInfo = wifiManager.getConnectionInfo();
                if (wifiInfo == null) {
                    return "0";
                }
                return wifiInfo.getBSSID();
            } catch (SecurityException e) {
                e.printStackTrace();
            }
            return "0";
        }


        /**
         * 获取运营商代码
         */
        public static int getOperator() {

            int code = Constants.MOBILE_UNKNOWN;

            if (sContext != null) {

                try {
                    TelephonyManager telManager = (TelephonyManager) sContext.getSystemService
                            (Context.TELEPHONY_SERVICE);
                    String operator = telManager.getSimOperator();

                    if (operator != null) {
                        if (operator.equals("46000") || operator.equals("46002")
                                || operator.equals("46007") || operator.equals("46020")) {

                            /** 中国移动 */
                            code = Constants.MOBILE_CHINAMOBILE;
                        } else if (operator.equals("46001") || operator.equals("46006")) {

                            /** 中国联通 */
                            code = Constants.MOBILE_UNICOM;
                        } else if (operator.equals("46003") || operator.equals("46005")) {

                            /** 中国电信 */
                            code = Constants.MOBILE_TELCOM;
                        }

                    }
                } catch (Exception e) {

                    e.printStackTrace();
                }
            }

            return code;
        }


        /**
         * 获取自定义当前联网类型
         *
         * @return 网络类型。-1为网络不可用；0为未知网络；1为WIFI；2为2G；3为3G；4为4G
         */
        public static int getNetworkType() {

            try {

                if (sContext != null) {

                    ConnectivityManager connectivity = (ConnectivityManager) sContext
                            .getSystemService(Context.CONNECTIVITY_SERVICE);
                    if (connectivity == null) {
                        return Constants.NETWORK_TYPE_UNKNOWN;
                    }

                    NetworkInfo activeNetInfo = connectivity.getActiveNetworkInfo();
                    if (activeNetInfo == null) {
                        return Constants.NETWORK_TYPE_UNCONNECTED;
                    }

                    if (!activeNetInfo.isAvailable() || !activeNetInfo.isConnected()) {
                        return Constants.NETWORK_TYPE_UNCONNECTED;
                    }

                    if (activeNetInfo.getType() == ConnectivityManager.TYPE_WIFI) {
                        return Constants.NETWORK_TYPE_WIFI;

                    } else if (activeNetInfo.getType() == ConnectivityManager.TYPE_MOBILE) {

                        switch (activeNetInfo.getSubtype()) {
                            case TelephonyManager.NETWORK_TYPE_CDMA:// ~ 14-64 kbps
                            case TelephonyManager.NETWORK_TYPE_IDEN:// ~25 kbps
                            case TelephonyManager.NETWORK_TYPE_1xRTT:// ~ 50-100 kbps
                            case TelephonyManager.NETWORK_TYPE_EDGE:// ~ 50-100 kbps
                            case TelephonyManager.NETWORK_TYPE_GPRS:// ~ 100 kbps
                                return Constants.NETWORK_TYPE_2G;

                            case TelephonyManager.NETWORK_TYPE_EVDO_0:// ~ 400-1000 kbps
                            case TelephonyManager.NETWORK_TYPE_UMTS:// ~ 400-7000 kbps
                            case TelephonyManager.NETWORK_TYPE_EVDO_A:// ~ 600-1400 kbps
                            case TelephonyManager.NETWORK_TYPE_HSPA:// ~ 700-1700 kbps
                            case TelephonyManager.NETWORK_TYPE_HSUPA:// ~ 1-23 Mbps
                            case TelephonyManager.NETWORK_TYPE_HSDPA:// ~ 2-14 Mbps
                            case 15: // 对应TelephonyManager.NETWORK_TYPE_HSPAP: 在api level
                                // 13下没有此值，但存在此网络类型，下面直接用数值代替
                                return Constants.NETWORK_TYPE_3G;

                            case 13: // 对应TelephonyManager.NETWORK_TYPE_LTE
                                return Constants.NETWORK_TYPE_4G;

                            case TelephonyManager.NETWORK_TYPE_UNKNOWN:
                            default:
                                return Constants.NETWORK_TYPE_UNKNOWN;
                        }
                    } else {
                        return Constants.NETWORK_TYPE_UNKNOWN;
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            return Constants.NETWORK_TYPE_UNCONNECTED;
        }

        /**
         * 获取wifi运营商
         *
         * @return 0, because we can not distinguish different operator, it is unnecessary.
         */
        public static int getWifiOperator() {
            return 0;
        }

        /**
         * 获取WifiSSID
         */
        public static String getWifiSSID(Context context) {
            WifiManager wifiMgr = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
            WifiInfo info = wifiMgr.getConnectionInfo();
            return info != null ? info.getSSID() : null;
        }

    }

}
