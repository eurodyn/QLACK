package com.eurodyn.qlack.fuse.security.config;

import com.eurodyn.qlack.fuse.aaa.service.UserService;
import com.eurodyn.qlack.fuse.security.providers.AAAUsernamePasswordProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author EUROPEAN DYNAMICS SA
 */
@Configuration
public class AuthConfig {

    @Autowired
    private UserService userService;

    @Bean
    public AAAUsernamePasswordProvider authenticationProvider() {
        AAAUsernamePasswordProvider authProvider = new AAAUsernamePasswordProvider();
        authProvider.setUserDetailsService(userService);
        return authProvider;
    }

}
