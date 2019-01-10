package com.eurodyn.qlack.fuse.security.cache;

import java.util.Objects;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.CacheManager;
import org.springframework.cache.concurrent.ConcurrentMapCache;
import org.springframework.security.core.userdetails.UserCache;
import org.springframework.security.core.userdetails.cache.SpringCacheBasedUserCache;
import org.springframework.stereotype.Component;

/**
 * Creates a default user cache based on
 * the system's configured caching mechanism.
 *
 * @author EUROPEAN DYNAMICS SA
 */
@Component
public class AAAUserCaching {

    private CacheManager cacheManager;

    private String cacheName = "users";

    private UserCache userCache;

    @Autowired
    public AAAUserCaching(CacheManager cacheManager) throws Exception {
        this.cacheManager = cacheManager;
        this.userCache = new SpringCacheBasedUserCache(Objects.requireNonNull(cacheManager.getCache(cacheName)));
    }

    public UserCache getUserCache() {
        return userCache;
    }

    public String getCacheName() {
        return cacheName;
    }

    public void setCacheName(String cacheName) {
        this.cacheName = cacheName;
    }

}
