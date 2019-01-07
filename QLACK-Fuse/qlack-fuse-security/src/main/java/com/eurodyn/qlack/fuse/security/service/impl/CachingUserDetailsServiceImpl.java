package com.eurodyn.qlack.fuse.security.service.impl;

import com.eurodyn.qlack.fuse.security.cache.DefaultAAAUserCaching;
import com.eurodyn.qlack.fuse.security.service.CachingUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.core.userdetails.UserCache;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

/**
 * @author EUROPEAN DYNAMICS SA
 */
@Service
@Qualifier("cachingUserDetailsService")
public class CachingUserDetailsServiceImpl implements CachingUserDetailsService {

    private final DefaultAAAUserCaching caching;

    private final UserDetailsService delegate;

    private UserCache userCache;

    @Autowired
    public CachingUserDetailsServiceImpl(DefaultAAAUserCaching caching, UserDetailsService delegate) {
        this.caching = caching;
        this.delegate = delegate;
        userCache = caching.getUserCache();
    }

    @Override
    public UserDetails loadUserByUsername(String username) {
        UserDetails user = userCache.getUserFromCache(username);

        if (user == null) {
            user = delegate.loadUserByUsername(username);
        }

        if (user != null) {
            userCache.putUserInCache(user);
        }

        return user;
    }

    @Override
    public UserCache getUserCache() {
        return userCache;
    }

}
