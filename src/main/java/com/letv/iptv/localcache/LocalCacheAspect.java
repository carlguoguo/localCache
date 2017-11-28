package com.letv.iptv.localcache;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;

/**
 * @author guoyunfeng
 */
@Aspect
@Component
public class LocalCacheAspect {
    @Autowired
    LocalCacheManager localCacheManager;

    @Around("@annotation(com.letv.iptv.localcache.LocalCacheAnnotation)")
    public Object aroundExec(ProceedingJoinPoint pjp) throws Throwable{
        String cacheBucketName = this.getCacheName(pjp);
        LocalCacheInterface<Object, Object> localCache = localCacheManager.getCache(cacheBucketName);
        String cacheKey = this.generateCacheKey(pjp);
        long expireTimeInMillis = this.getExpireInMillis(pjp);
        Object result = null;
        try {
            // 先从LocalCache中拿
            result = localCache.get(cacheKey);
        } catch (NoSuchKeyException | CacheExpiredException e) {
            // 如果缓存没有该键或者缓存过期，则从数据库里取一遍
            result = pjp.proceed();
            localCache.set(cacheKey, result, expireTimeInMillis);
        }
        return result;
    }

    private String generateCacheKey(JoinPoint joinPoint) {
        StringBuilder sb = new StringBuilder();
        Object[] args = joinPoint.getArgs();
        for (Object arg: args) {
            sb.append(arg.toString());
            sb.append("_");
        }
        return sb.toString();
    }

    private String getCacheName(JoinPoint joinPoint) {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();
        LocalCacheAnnotation localCacheAnnotation = method.getAnnotation(LocalCacheAnnotation.class);
        if (localCacheAnnotation.name() != null && !localCacheAnnotation.name().isEmpty()) {
            return localCacheAnnotation.name();
        } else {
            return method.getName();
        }
    }

    private long getExpireInMillis(JoinPoint joinPoint) {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();
        LocalCacheAnnotation localCacheAnnotation = method.getAnnotation(LocalCacheAnnotation.class);
        if (localCacheAnnotation.expireInMillis() > 0) {
            return localCacheAnnotation.expireInMillis();
        } else {
            return LocalCacheInterface.DEFAULT_EXPIRE_TIME;
        }
    }
}
