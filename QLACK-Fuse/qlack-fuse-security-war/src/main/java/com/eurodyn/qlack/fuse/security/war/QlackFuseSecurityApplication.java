package com.eurodyn.qlack.fuse.security.war;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EnableJpaRepositories("com.eurodyn.qlack.fuse.aaa.repository")
@EntityScan("com.eurodyn.qlack.fuse.aaa.model")
@ComponentScan(basePackages = {
    "com.eurodyn.qlack.fuse.aaa.service",
    "com.eurodyn.qlack.fuse.aaa.mappers",
    "com.eurodyn.qlack.fuse.security"
})
public class QlackFuseSecurityApplication {

    public static void main(String[] args) {
        SpringApplication.run(QlackFuseSecurityApplication.class, args);
    }
}
