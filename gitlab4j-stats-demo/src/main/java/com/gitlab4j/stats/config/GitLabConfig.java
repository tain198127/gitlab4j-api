package com.gitlab4j.stats.config;

import org.gitlab4j.api.GitLabApi;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class GitLabConfig {

    private static final Logger logger = LoggerFactory.getLogger(GitLabConfig.class);

    @Value("${gitlab.api.url}")
    private String gitLabApiUrl;

    @Value("${gitlab.api.token}")
    private String gitLabApiToken;

    @Bean
    public GitLabApi gitLabApi() {
        logger.info("Initializing GitLabApi with URL: {} and token: {}...", gitLabApiUrl, 
                   gitLabApiToken != null ? gitLabApiToken.substring(0, Math.min(10, gitLabApiToken.length())) + "..." : "null");
        
        // Handle empty or null values gracefully
        if (gitLabApiUrl == null || gitLabApiUrl.trim().isEmpty() || 
            gitLabApiToken == null || gitLabApiToken.trim().isEmpty()) {
            logger.warn("GitLab API URL or token is not configured properly. Using dummy values.");
            // Use dummy configuration for demo purposes - use base URL without /api/v4
            return new GitLabApi(GitLabApi.ApiVersion.V4, "https://gitlab.com", "dummy-token");
        }
        
        // Remove /api/v4 from the URL if it exists, as GitLabApi will add it automatically
        String cleanUrl = gitLabApiUrl;
        if (gitLabApiUrl.endsWith("/api/v4")) {
            cleanUrl = gitLabApiUrl.substring(0, gitLabApiUrl.length() - 7); // Remove "/api/v4"
            logger.info("Removed /api/v4 from URL, using base URL: {}", cleanUrl);
        }
        
        try {
            GitLabApi gitLabApi = new GitLabApi(GitLabApi.ApiVersion.V4, cleanUrl, gitLabApiToken);
            logger.info("GitLabApi initialized successfully");
            return gitLabApi;
        } catch (Exception e) {
            logger.error("Failed to initialize GitLabApi, using dummy configuration", e);
            return new GitLabApi(GitLabApi.ApiVersion.V4, "https://gitlab.com", "dummy-token");
        }
    }
}
