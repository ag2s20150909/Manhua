package cn.jony.okhttpplus.lib.httpdns.model;


import cn.jony.okhttpplus.lib.httpdns.strategy.HostResolveStrategy;

import static cn.jony.okhttpplus.lib.httpdns.DNSCacheConfig.MAX_RTT;
import static cn.jony.okhttpplus.lib.httpdns.DNSCacheConfig.MAX_TTL;

public class HostIP {
    public String sourceIP;
    public String targetIP;
    public String operator;
    public String host;

    public long saveMillis;
    public long rtt;
    public int ttl = MAX_TTL;
    private static final long FAIL_RTT = (long) (1.2 * MAX_RTT);

    private int sucNum;
    private int failNum;
    public int visitSinceSaved;

    public HostIP() {
    }

    private HostIP(Builder builder) {
        sourceIP = builder.sourceIP;
        targetIP = builder.targetIP;
        operator = builder.operator;
        host = builder.host;
        saveMillis = builder.saveMillis;
        rtt = builder.rtt;
        ttl = builder.ttl;
        sucNum = builder.sucNum;
        failNum = builder.failNum;
        visitSinceSaved = builder.visitSinceSaved;
    }

    public boolean isReliable(HostResolveStrategy hostResolveStrategy) {
        return hostResolveStrategy.isReliable(this);
    }

    public long getWorkMillis() {
        return System.currentTimeMillis() - saveMillis;
    }

    public double getFailPercent() {
        return sucNum > 0 ? failNum / (double) sucNum : 1.0;
    }

    public boolean hasUsed() {
        return sucNum + failNum > 0;
    }

    public void updateRtt(long rtt, boolean isSuc) {
        if (isSuc) {
            sucNum++;
            this.rtt += rtt;
        } else {
            failNum++;
            this.rtt += rtt > 0 ? rtt : FAIL_RTT;
        }
        this.rtt /= usedNum();
    }

    private int usedNum() {
        return sucNum + failNum;
    }

    public static final class Builder {
        private String sourceIP;
        private String targetIP;
        private String operator;
        private String host;
        private long saveMillis;
        private long rtt;
        private int ttl = MAX_TTL;
        private int sucNum;
        private int failNum;
        private int visitSinceSaved;

        public Builder() {
        }

        public Builder sourceIP(String val) {
            sourceIP = val;
            return this;
        }

        public Builder targetIP(String val) {
            targetIP = val;
            return this;
        }

        public Builder operator(String val) {
            operator = val;
            return this;
        }

        public Builder host(String val) {
            host = val;
            return this;
        }

        public Builder saveMillis(long val) {
            saveMillis = val;
            return this;
        }

        public Builder rtt(long val) {
            rtt = val;
            return this;
        }

        public Builder ttl(int val) {
            ttl = val;
            return this;
        }

        public Builder sucNum(int val) {
            sucNum = val;
            return this;
        }

        public Builder failNum(int val) {
            failNum = val;
            return this;
        }

        public Builder visitSinceSaved(int val) {
            visitSinceSaved = val;
            return this;
        }

        public HostIP build() {
            return new HostIP(this);
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        HostIP ip = (HostIP) o;

        return targetIP != null ? targetIP.equals(ip.targetIP) : ip.targetIP == null;

    }

    @Override
    public int hashCode() {
        return targetIP != null ? targetIP.hashCode() : 0;
    }

    public int getSucNum() {
        return sucNum;
    }

    public void setSucNum(int sucNum) {
        this.sucNum = sucNum;
    }

    public int getFailNum() {
        return failNum;
    }

    public void setFailNum(int failNum) {
        this.failNum = failNum;
    }

    public static class Key {
        public String sourceId;
        public String host;

        public Key(String host, String sourceId) {
            this.sourceId = sourceId;
            this.host = host;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            Key key = (Key) o;

            if (sourceId != null ? !sourceId.equals(key.sourceId) : key.sourceId != null)
                return false;
            return host != null ? host.equals(key.host) : key.host == null;

        }

        @Override
        public int hashCode() {
            int result = sourceId != null ? sourceId.hashCode() : 0;
            result = 31 * result + (host != null ? host.hashCode() : 0);
            return result;
        }
    }
}
