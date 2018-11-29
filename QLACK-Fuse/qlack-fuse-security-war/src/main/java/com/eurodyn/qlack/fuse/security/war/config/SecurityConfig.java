package com.eurodyn.qlack.fuse.security.war.config;

import com.eurodyn.qlack.fuse.aaa.service.UserService;
import com.eurodyn.qlack.fuse.security.access.AAAPermissionEvaluator;
import com.eurodyn.qlack.fuse.security.providers.AAAProvider;
import javax.sql.DataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.access.expression.DefaultWebSecurityExpressionHandler;
import org.springframework.security.web.authentication.www.BasicAuthenticationEntryPoint;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Value("${cxf.path}")
    private String cxfPath;

    private final DataSource dataSource;

    private final UserService userService;

    @Autowired
    private AAAPermissionEvaluator aaaPermissionEvaluator;

    @Autowired
    public SecurityConfig(DataSource dataSource, UserService userService) {
        this.dataSource = dataSource;
        this.userService = userService;
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.authenticationProvider(authenticationProvider())
            .jdbcAuthentication()
            .dataSource(dataSource)
            .rolePrefix("")
            // .usersByUsernameQuery(usersQuery)
            // .authoritiesByUsernameQuery(rolesQuery)
            .passwordEncoder(passwordEncoder());
    }

    @Override
    public void configure(WebSecurity web) {
        DefaultWebSecurityExpressionHandler handler = new DefaultWebSecurityExpressionHandler();
        handler.setPermissionEvaluator(new AAAPermissionEvaluator());
        web.expressionHandler(handler);
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable()
            .authorizeRequests()
            .expressionHandler(webExpressionHandler())
            .antMatchers(cxfPath + "/auth/unauthorized").permitAll()
            .antMatchers(cxfPath + "/auth/authorized").access("hasPermission(principal,'auth')")
            .anyRequest().authenticated()
            // .anyRequest().permitAll()
            .and().httpBasic().realmName("REALM").authenticationEntryPoint(basicAuthEntryPoint())
            .and().sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
    }

    @Bean
    public DefaultWebSecurityExpressionHandler webExpressionHandler() {
        DefaultWebSecurityExpressionHandler webSecurityExpressionHandler = new DefaultWebSecurityExpressionHandler();
        webSecurityExpressionHandler.setPermissionEvaluator(new AAAPermissionEvaluator());
        return webSecurityExpressionHandler;
    }

    @Bean
    public BasicAuthenticationEntryPoint basicAuthEntryPoint() {
        BasicAuthenticationEntryPoint baep = new BasicAuthenticationEntryPoint();
        baep.setRealmName("REALM");
        return baep;
    }

    @Bean
    public AAAProvider authenticationProvider() {
        AAAProvider authProvider = new AAAProvider();
        authProvider.setUserDetailsService(userService);
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
    }

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

}
