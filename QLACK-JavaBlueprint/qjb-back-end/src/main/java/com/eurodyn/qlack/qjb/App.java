package com.eurodyn.qlack.qjb;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EnableJpaRepositories({"com.eurodyn"})
@ComponentScan({"com.eurodyn"})
@EntityScan({"com.eurodyn"})
public class App {

  public static void main(String[] args) {
    SpringApplication.run(App.class, args);
  }
}
