package com.eurodyn.qlack.fuse.security.cache;

import org.springframework.cache.concurrent.ConcurrentMapCache;
import org.springframework.security.core.userdetails.UserCache;
import org.springframework.security.core.userdetails.cache.SpringCacheBasedUserCache;
import org.springframework.stereotype.Component;

/**
 * Sets the a ConcurrentMapCache as the default caching mechanism for AAA users.
 *
 * @author EUROPEAN DYNAMICS SA
 */
@Component
public class DefaultAAAUserCaching {

    private String cacheName = "users";

    private UserCache userCache;

    public DefaultAAAUserCaching() throws Exception {
        userCache = new SpringCacheBasedUserCache(new ConcurrentMapCache(cacheName, false));
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
