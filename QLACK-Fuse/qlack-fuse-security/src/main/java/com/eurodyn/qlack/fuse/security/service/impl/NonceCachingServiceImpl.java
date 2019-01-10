package com.eurodyn.qlack.fuse.security.service.impl;

import com.eurodyn.qlack.fuse.security.service.NonceCachingService;
import java.util.Objects;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.stereotype.Service;

/**
 * @author EUROPEAN DYNAMICS SA
 */
@Service
public class NonceCachingServiceImpl implements NonceCachingService {

    private CacheManager cacheManager;

    private static final String NONCE_CACHE_PREFIX = "nonce-";

    @Autowired
    public NonceCachingServiceImpl(CacheManager cacheManager) {
        this.cacheManager = cacheManager;
    }

    @Override
    public <T> T getValueForUser(String username, String nonce, Class<T> type) {
        return Objects.requireNonNull(cacheManager.getCache(NONCE_CACHE_PREFIX + username)).get(nonce, type);
    }

    @Override
    public void putForUser(String username, String nonce, Object value) {
        Objects.requireNonNull(cacheManager.getCache(NONCE_CACHE_PREFIX + username)).put(nonce, value);
    }

    @Override
    public void clear(String username) {
        Optional.ofNullable(cacheManager.getCache(NONCE_CACHE_PREFIX + username)).ifPresent(Cache::clear);
    }

}
