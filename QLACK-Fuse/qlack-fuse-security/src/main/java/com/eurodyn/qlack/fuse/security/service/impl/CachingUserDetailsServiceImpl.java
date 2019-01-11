package com.eurodyn.qlack.fuse.security.service.impl;

import com.eurodyn.qlack.fuse.security.cache.AAAUserCaching;
import com.eurodyn.qlack.fuse.security.service.CachingUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
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

    private final AAAUserCaching userCaching;

    private final UserDetailsService delegate;

    @Autowired
    public CachingUserDetailsServiceImpl(AAAUserCaching userCaching, UserDetailsService delegate) {
        this.userCaching = userCaching;
        this.delegate = delegate;
    }

    @Override
    public UserDetails loadUserByUsername(String username) {
        UserCache userCache = userCaching.getUserCache();

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
    public void removeUser(String username) {
        userCaching.getUserCache().removeUserFromCache(username);
    }

    @Override
    public UserCache getUserCache() {
        return userCaching.getUserCache();
    }

}
