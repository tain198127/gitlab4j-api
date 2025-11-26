package com.example.gitlab4jstatsdemo.controller;

import com.example.gitlab4jstatsdemo.entity.CommitDetail;
import com.example.gitlab4jstatsdemo.entity.DailyStats;
import com.example.gitlab4jstatsdemo.entity.DeveloperStats;
import com.example.gitlab4jstatsdemo.entity.ProjectStats;
import com.example.gitlab4jstatsdemo.repository.CommitDetailRepository;
import com.example.gitlab4jstatsdemo.repository.DailyStatsRepository;
import com.example.gitlab4jstatsdemo.repository.DeveloperStatsRepository;
import com.example.gitlab4jstatsdemo.repository.ProjectStatsRepository;
import com.example.gitlab4jstatsdemo.service.GitLabDataCollectionService;
import org.gitlab4j.api.GitLabApiException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/stats")
public class StatsController {
    private final GitLabDataCollectionService gitLabDataCollectionService;
    private final DeveloperStatsRepository developerStatsRepository;
    private final DailyStatsRepository dailyStatsRepository;
    private final ProjectStatsRepository projectStatsRepository;
    private final CommitDetailRepository commitDetailRepository;

    public StatsController(
            GitLabDataCollectionService gitLabDataCollectionService,
            DeveloperStatsRepository developerStatsRepository,
            DailyStatsRepository dailyStatsRepository,
            ProjectStatsRepository projectStatsRepository,
            CommitDetailRepository commitDetailRepository) {
        this.gitLabDataCollectionService = gitLabDataCollectionService;
        this.developerStatsRepository = developerStatsRepository;
        this.dailyStatsRepository = dailyStatsRepository;
        this.projectStatsRepository = projectStatsRepository;
        this.commitDetailRepository = commitDetailRepository;
    }

    @PostMapping("/collect")
    public ResponseEntity<String> collectData() {
        try {
            gitLabDataCollectionService.collectCommitData();
            return ResponseEntity.ok("Data collection completed successfully");
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body("Data collection failed: " + e.getMessage());
        }
    }

    @GetMapping("/developers")
    public ResponseEntity<List<DeveloperStats>> getDeveloperStats() {
        return ResponseEntity.ok(developerStatsRepository.findAll());
    }

    @GetMapping("/daily")
    public ResponseEntity<List<DailyStats>> getDailyStats() {
        return ResponseEntity.ok(dailyStatsRepository.findAll());
    }

    @GetMapping("/project")
    public ResponseEntity<List<ProjectStats>> getProjectStats() {
        return ResponseEntity.ok(projectStatsRepository.findAll());
    }

    @GetMapping("/commits")
    public ResponseEntity<List<CommitDetail>> getCommitDetails() {
        return ResponseEntity.ok(commitDetailRepository.findAll());
    }
}