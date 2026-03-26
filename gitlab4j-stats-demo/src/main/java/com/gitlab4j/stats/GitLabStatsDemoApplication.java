package com.gitlab4j.stats;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableAsync
@EnableScheduling
@ComponentScan(basePackages = {"com.gitlab4j.stats"})
public class GitLabStatsDemoApplication {

    public static void main(String[] args) {
        SpringApplication.run(GitLabStatsDemoApplication.class, args);
    }
}
