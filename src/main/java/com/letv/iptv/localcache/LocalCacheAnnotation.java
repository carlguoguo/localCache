package com.letv.iptv.localcache;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.METHOD })
public @interface LocalCacheAnnotation {

    /**
     * LocalCache名称
     * @return
     */
    String name() default "";

    /**
     * 过期时间
     * @return
     */
    long expireInMillis() default 0L;
}
