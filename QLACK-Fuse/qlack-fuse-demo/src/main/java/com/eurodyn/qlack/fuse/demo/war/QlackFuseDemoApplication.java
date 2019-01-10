package com.eurodyn.qlack.fuse.demo.war;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EnableJpaRepositories("com.eurodyn.qlack.fuse.aaa.repository")
@EntityScan("com.eurodyn.qlack.fuse.aaa.model")
@ComponentScan(basePackages = {
    "com.eurodyn.qlack.util.swagger",
    "com.eurodyn.qlack.fuse.aaa",
    "com.eurodyn.qlack.fuse.security",
    "com.eurodyn.qlack.fuse.demo.*"
})

public class QlackFuseDemoApplication {
    public static void main(String[] args) {SpringApplication.run(QlackFuseDemoApplication.class, args);
    }
}