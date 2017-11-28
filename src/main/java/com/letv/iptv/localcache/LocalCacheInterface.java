package com.letv.iptv.localcache;

/**
 * @author guoyunfeng
 */
public interface LocalCacheInterface<K, V> {
    /**
     * 默认缓存5分钟
     */
    final static long DEFAULT_EXPIRE_TIME = 1000 * 60 * 5;

    /**
     * 添加缓存
     * @param key
     * @param value
     * @param expireTimeInMillis 过期时间，单位毫秒
     */
    void set(K key, V value, long expireTimeInMillis);

    /**
     * 获取缓存，传入flush=true可强制清除缓存
     * @param key
     * @return 如果有键，返回对应的值
     * @throws NoSuchKeyException 如果没有键
     * @throws CacheExpiredException 缓存过期
     */
    V get(K key) throws NoSuchKeyException, CacheExpiredException;


    /**
     * 清除缓存
     * @param key
     */
    void flush(K key);

    /**
     * 清除所有缓存
     */
    void flushAll();
}