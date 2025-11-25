package com.gitlab4j.stats.config;

import org.gitlab4j.api.GitLabApi;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class GitLabConfig {

    @Value("${gitlab.api.url}")
    private String gitLabApiUrl;

    @Value("${gitlab.api.token}")
    private String gitLabApiToken;

    @Bean
    public GitLabApi gitLabApi() {
        return new GitLabApi(gitLabApiUrl, gitLabApiToken);
    }
}