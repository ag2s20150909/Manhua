package cn.jony.okhttpplus.lib.httpdns.strategy;


import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.List;

import cn.jony.okhttpplus.lib.httpdns.model.HostIP;

public interface HostResolveStrategy {
    /**
     * 使用HttpDNS解析的策略 <br />
     * default - DNS解析流程包括 <br />
     * 1. 查找 <br />
     * 查找索引包括host和sourceIP，依次分3个层次cache，db和httpDNS查找；在任何一层如果查找到数据则返回，并用查找出来的数据刷新上一层次的数据；
     * 如果都没有查找到，则走系统默认的dns；默认的httpDNS查找是异步的，因此第一次可能不能及时的走httpDNS，如果希望第一次查找就走dns，可以使用
     * {@link cn.jony.okhttpplus.lib.httpdns.DNSCache#preLoadDNS(String...)}来进行预加载<br />
     * 2. 更新 <br />
     * 分为定时更新和非定时更新: <br />
     * a. 定时更新：只更新db中的ip数据，不更新cache中的数据，cache中的数据在超时时，从db中查找或者通过httpDNS请求返回数据更新cache。
     * 定时更新流程：每隔超时时间的一半时间，对db所有的HostIP数据进行一轮测速并统计，并重置缓存时间内访问量超过阈值的HostIP的缓存时间；最后
     * 删除不可靠的HostIP，保留可靠的HostIP。<br />
     * b. 非定时更新，主要包括两个方面：1. 当cache数据失效时，从db获得有效数据时直接更新cache；2. 通过DnsVisitInterceptor对访问的dns进行
     * 数据更新。<br />
     * 3. 验证HostIp可靠性： <br />
     * a. targetIP 存在 <br />
     * b. 没有超时 <br />
     * c. rtt 没有超过允许的最大rtt，默认300 <br />
     * d. ttl 没有超过允许的最大ttl，默认300 <br />
     * e. 请求失败次数最多为1次，或者成功率大于95% <br />
     * <br />
     * strict - 严格模式，与default模式的差别主要在于default模式会优先使用未使用过的dns（HostIP的rtt默认为0）；而strict模式确保HostIP只有在经过
     * 测速后才可以使用，因此strict模式在HttpDNS请求后会尽快发送一轮测速，而default模式只会在定时更新任务中进行测速。 <br />
     * <br />
     * sync - 同步模式，default在发出HttpDNS请求后并不进行等待，直接返回结果。这时结果可能为null，即本次请求下次使用。而sync模式等待请求完成后再返回，
     * 一般依次dns请求的响应时间在20ms以内。
     */
    String DEFAULT = "default";
    String STRICT = "strict";
    String SYNC = "sync";
    String EMPTY = "empty";

    HostResolveStrategy EMPTY_RESOLVE_STRATEGY = new HostResolveStrategy() {
        @Override
        public List<InetAddress> lookup(String hostname) {
            return null;
        }

        @Override
        public boolean isReliable(HostIP ip) {
            return false;
        }

        @Override
        public void update() {

        }

        @Override
        public void update(HostIP ip) {

        }

        @Override
        public void clear() {

        }
    };

    /**
     * lookup dns of hostname; if hostname has been ip, then the hostname of InetAddress is ""
     *
     * @param hostname
     * @return
     * @throws UnknownHostException
     */
    List<InetAddress> lookup(String hostname) throws UnknownHostException;

    boolean isReliable(HostIP ip);

    void update();

    void update(HostIP ip);

    void clear();
}
