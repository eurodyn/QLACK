package com.eurodyn.qlack.fuse.security.config;

import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * @author European Dynamics
 */
@Configuration
@EnableAutoConfiguration
@EnableJpaRepositories(basePackages = {"com.eurodyn.qlack.fuse.aaa.repository"})
@EntityScan(basePackages = {"com.eurodyn.qlack.fuse.aaa.model"})
@ComponentScan(basePackages = {"com.eurodyn.qlack.fuse.aaa.service",
    "com.eurodyn.qlack.fuse.aaa.mappers",
    "com.eurodyn.qlack.fuse.aaa.config",
    "com.eurodyn.qlack.fuse.aaa.ws",
    "com.eurodyn.qlack.fuse.security.providers"
})
@EnableTransactionManagement
public class IntegrationTestConfig {

}
