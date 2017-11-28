package com.letv.iptv.localcache;

import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.Collections;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * @author guoyunfeng
 * 所有LocalCache实例的管理类，主要负责同步创建和获取LocalCache实例，保证相同LocalCache不重复初始化
 */
@Component
public class LocalCacheManager {

    private final ConcurrentMap<String, LocalCacheInterface> cacheMap = new ConcurrentHashMap<>(16);

    public Collection<String> getCacheNames() {
        return Collections.unmodifiableSet(this.cacheMap.keySet());
    }

    public LocalCacheInterface getCache(String name) {
        LocalCacheInterface cache = this.cacheMap.get(name);
        if (cache == null) {
            synchronized (this.cacheMap) {
                cache = this.cacheMap.get(name);
                if (cache == null) {
                    cache = new LocalCache(name);
                    this.cacheMap.put(name, cache);
                }
            }
        }
        return cache;
    }
}
