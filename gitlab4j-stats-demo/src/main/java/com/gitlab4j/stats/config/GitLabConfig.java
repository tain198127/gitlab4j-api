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
        // Handle empty or null values gracefully
        if (gitLabApiUrl == null || gitLabApiUrl.trim().isEmpty() || 
            gitLabApiToken == null || gitLabApiToken.trim().isEmpty()) {
            // Return a dummy GitLabApi that won't cause issues during startup
            return new GitLabApi("https://gitlab.com", "dummy-token");
        }
        return new GitLabApi(gitLabApiUrl, gitLabApiToken);
    }
}
