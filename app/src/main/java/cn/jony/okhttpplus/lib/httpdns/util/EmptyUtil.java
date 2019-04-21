package cn.jony.okhttpplus.lib.httpdns.util;


import java.util.Collection;

public class EmptyUtil {
    public static <E> boolean isCollectionEmpty(Collection<E> collection) {
        return collection == null || collection.isEmpty();
    }
}
