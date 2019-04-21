package cn.jony.okhttpplus.lib.httpdns.strategy;


public class HostResolveFactory {
    public static HostResolveStrategy getStrategy(String strategy) {
        switch (strategy) {
            case HostResolveStrategy.EMPTY:
                return HostResolveStrategy.EMPTY_RESOLVE_STRATEGY;
            case HostResolveStrategy.STRICT:
                return new StrictHostResolveStrategy();
            case HostResolveStrategy.SYNC:
                return new SyncHostResolveStrategy();
            case HostResolveStrategy.DEFAULT:
            default:
                return new DefaultHostResolveStrategy();
        }
    }
}
