package com.eurodyn.qlack.fuse.security.service;

import org.springframework.security.core.userdetails.UserCache;
import org.springframework.security.core.userdetails.UserDetailsService;

/**
 * A service responsible for user management in the configured system cache.
 *
 * @author EUROPEAN DYNAMICS SA
 */
public interface CachingUserDetailsService extends UserDetailsService {

    /**
     * Removes user from cache by username.
     * If the user doesn't exist it returns without throwing an exception.
     *
     * @param username User name to be evicted from cache
     */
    void removeUser(String username);

    /**
     * Returns the cache implementation used for caching the user details.
     *
     * @return User cache
     */
    UserCache getUserCache();

}
