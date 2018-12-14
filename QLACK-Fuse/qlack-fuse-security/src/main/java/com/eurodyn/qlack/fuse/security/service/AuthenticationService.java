package com.eurodyn.qlack.fuse.security.service;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;

public interface AuthenticationService {

    /**
     * Provides access to Spring's authentication method.
     *
     * @param authentication Authentication object with user credentials
     * @return Authentication object with user details
     * @throws AuthenticationException If authentication fails
     */
    Authentication authenticate(Authentication authentication) throws AuthenticationException;

    /**
     * Provides access to Spring's authentication method.
     *
     * @param username Username
     * @param password User password
     * @return Authentication object with user details
     * @throws AuthenticationException If authentication fails
     */
    Authentication authenticate(String username, String password) throws AuthenticationException;

}
