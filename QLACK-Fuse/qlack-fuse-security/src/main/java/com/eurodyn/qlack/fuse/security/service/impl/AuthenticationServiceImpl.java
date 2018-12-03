package com.eurodyn.qlack.fuse.security.service.impl;

import com.eurodyn.qlack.fuse.security.service.AuthenticationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

@Service
public class AuthenticationServiceImpl implements AuthenticationService {

    @Autowired
    private AuthenticationProvider authenticationProvider;

    @Override
    public Authentication authenticate(Authentication authentication) {
       return authenticationProvider.authenticate(authentication);
    }

}
