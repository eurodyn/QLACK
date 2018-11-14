package com.eurodyn.qlack.fuse.security.war.config;

import com.eurodyn.qlack.fuse.security.manager.AAAProvider;
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
import org.springframework.security.web.authentication.www.BasicAuthenticationEntryPoint;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Value("${cxf.path}")
    private String cxfPath;

    @Value("${spring.security.users-query}")
    private String usersQuery;

    // @Value("${spring.security.roles-query}")
    // private String rolesQuery;

    private final AAAProvider authProvider;

    private final DataSource dataSource;

    @Autowired
    public SecurityConfig(DataSource dataSource, AAAProvider authProvider) {
        this.dataSource = dataSource;
        this.authProvider = authProvider;
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.authenticationProvider(authProvider)
            .jdbcAuthentication()
            .dataSource(dataSource)
            .usersByUsernameQuery(usersQuery)
            // .authoritiesByUsernameQuery(rolesQuery)
            .passwordEncoder(passwordEncoder());
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable()
            .authorizeRequests()
            .antMatchers(cxfPath + "/auth/unauthorized").permitAll()
            .anyRequest().authenticated()
            // .anyRequest().permitAll()
            .and().httpBasic().realmName("REALM").authenticationEntryPoint(basicAuthEntryPoint())
            .and().sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
    }

    @Bean
    public BasicAuthenticationEntryPoint basicAuthEntryPoint() {
        BasicAuthenticationEntryPoint baep = new BasicAuthenticationEntryPoint();
        baep.setRealmName("REALM");
        return baep;
    }

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

}
