package cn.jony.okhttpplus.lib;


import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Arrays;

public class Test {
    public static void main(String[] args) throws UnknownHostException {
        InetAddress[] addresses = InetAddress.getAllByName
                ("www.baidu.com");
        System.out.println(Arrays.deepToString(addresses));
    }
}
