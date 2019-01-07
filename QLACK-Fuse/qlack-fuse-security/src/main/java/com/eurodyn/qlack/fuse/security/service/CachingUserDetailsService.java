package com.eurodyn.qlack.fuse.security.service;

import org.springframework.security.core.userdetails.UserCache;
import org.springframework.security.core.userdetails.UserDetailsService;

/**
 * @author EUROPEAN DYNAMICS SA
 */
public interface CachingUserDetailsService extends UserDetailsService {

    /**
     * Returns the cache implementation used for caching the user details.
     *
     * @return User cache
     */
    UserCache getUserCache();

}
