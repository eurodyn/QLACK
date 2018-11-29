package com.eurodyn.qlack.fuse.security.config;

import com.eurodyn.qlack.fuse.aaa.service.UserService;
import com.eurodyn.qlack.fuse.security.providers.AAAProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;

/**
 * @author EUROPEAN DYNAMICS SA
 */
@Configuration
public class AuthConfig {

    @Autowired
    private UserService userService;

    @Bean
    public AAAProvider authenticationProvider() {
        AAAProvider authProvider = new AAAProvider();
        authProvider.setUserDetailsService(userService);
        return authProvider;
    }

}
