package cn.jony.okhttpplus.lib.httpdns;


import cn.jony.okhttpplus.lib.httpdns.strategy.HostResolveStrategy;

public class DNSCacheConfig {
    private static final long EXPIRE_MILLIS = 300_000;

    public static final int MAX_TTL = 300;
    public static final int FAST_TTL = 64;
    public static final int MAX_CACHE_SIZE = 20;
    public static final long MAX_RTT = 300;

    public String hostResolveStrategyName;
    public HostResolveStrategy hostResolveStrategy;
    public long expireMillis;
    public long updateMillis;
    public int maxTtl;
    public long maxRtt;
    public int maxCacheSize;

    static DNSCacheConfig DEFAULT = new DNSCacheConfig(HostResolveStrategy.DEFAULT, null,
            EXPIRE_MILLIS, EXPIRE_MILLIS / 2, MAX_TTL, MAX_RTT, MAX_CACHE_SIZE);

    public DNSCacheConfig(String hostResolveStrategyName, HostResolveStrategy
            hostResolveStrategy, long expireMillis, long updateMillis, int maxTtl, long maxRtt, int
                                  maxCacheSize) {
        this.hostResolveStrategyName = hostResolveStrategyName;
        this.hostResolveStrategy = hostResolveStrategy;
        this.expireMillis = expireMillis;
        this.updateMillis = updateMillis;
        this.maxTtl = maxTtl;
        this.maxRtt = maxRtt;
        this.maxCacheSize = maxCacheSize;
    }

    private DNSCacheConfig(Builder builder) {
        hostResolveStrategyName = builder.hostResolveStrategyName;
        hostResolveStrategy = builder.hostResolveStrategy;
        expireMillis = builder.expireMillis;
        updateMillis = builder.updateMillis;
        maxTtl = builder.maxTtl;
        maxRtt = builder.maxRtt;
        maxCacheSize = builder.maxCacheSize;
    }

    public static class Builder {
        private String hostResolveStrategyName = HostResolveStrategy.DEFAULT;
        private HostResolveStrategy hostResolveStrategy;
        private long expireMillis = EXPIRE_MILLIS;
        private long updateMillis = expireMillis / 2;
        private int maxTtl = MAX_TTL;
        private long maxRtt = MAX_RTT;
        private int maxCacheSize = MAX_CACHE_SIZE;

        public Builder() {
        }

        public Builder hostResolveStrategyName(String hostResolveStrategyName) {
            this.hostResolveStrategyName = hostResolveStrategyName;
            return this;
        }

        public Builder hostResolveStrategy(HostResolveStrategy val) {
            hostResolveStrategy = val;
            return this;
        }

        public Builder expireMillis(long val) {
            expireMillis = val;
            return this;
        }

        public Builder updateMillis(long val) {
            updateMillis = val;
            return this;
        }

        public Builder maxTtl(int val) {
            maxTtl = val;
            return this;
        }

        public Builder maxRtt(long val) {
            maxRtt = val;
            return this;
        }

        public Builder maxCacheSize(int val) {
            maxCacheSize = val;
            return this;
        }

        public DNSCacheConfig build() {
            return new DNSCacheConfig(this);
        }
    }
}
