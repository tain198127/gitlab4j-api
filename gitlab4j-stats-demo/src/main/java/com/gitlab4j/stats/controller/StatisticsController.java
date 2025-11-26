package com.gitlab4j.stats.controller;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.gitlab4j.stats.dto.*;
import com.gitlab4j.stats.service.GitLabDataCollectionService;
import com.gitlab4j.stats.service.StatisticsService;

@RestController
@RequestMapping("/api/stats")
@CrossOrigin(origins = "*")
public class StatisticsController {

    @Autowired
    private StatisticsService statisticsService;

    @Autowired
    private GitLabDataCollectionService dataCollectionService;

    @GetMapping("/developers")
    public ResponseEntity<List<DeveloperStatsDTO>> getDeveloperStats(
            @RequestParam(defaultValue = "10") int limit, @RequestParam(defaultValue = "linesChanged") String sortBy) {
        List<DeveloperStatsDTO> stats = statisticsService.getDeveloperStats(limit, sortBy);
        return ResponseEntity.ok(stats);
    }

    @GetMapping("/developers/{email}")
    public ResponseEntity<DeveloperStatsDTO> getDeveloperStatsByEmail(@PathVariable String email) {
        DeveloperStatsDTO stats = statisticsService.getDeveloperStatsByEmail(email);
        return ResponseEntity.ok(stats);
    }

    @GetMapping("/daily")
    public ResponseEntity<List<DailyStatsDTO>> getDailyStats(
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,
            @RequestParam(required = false) String developerEmail) {
        List<DailyStatsDTO> stats = statisticsService.getDailyStats(startDate, endDate, developerEmail);
        return ResponseEntity.ok(stats);
    }

    @GetMapping("/projects")
    public ResponseEntity<List<ProjectStatsDTO>> getProjectStats() {
        List<ProjectStatsDTO> stats = statisticsService.getProjectStats();
        return ResponseEntity.ok(stats);
    }

    @GetMapping("/projects/{projectId}")
    public ResponseEntity<ProjectStatsDTO> getProjectStatsById(@PathVariable Integer projectId) {
        ProjectStatsDTO stats = statisticsService.getProjectStatsById(projectId);
        return ResponseEntity.ok(stats);
    }

    @GetMapping("/summary")
    public ResponseEntity<SummaryStatsDTO> getSummaryStats() {
        SummaryStatsDTO stats = statisticsService.getSummaryStats();
        return ResponseEntity.ok(stats);
    }

    @GetMapping("/activity")
    public ResponseEntity<List<DeveloperActivityDTO>> getDeveloperActivity(
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        List<DeveloperActivityDTO> activity = statisticsService.getDeveloperActivity(startDate, endDate);
        return ResponseEntity.ok(activity);
    }

    @GetMapping("/trends")
    public ResponseEntity<List<CommitTrendDTO>> getCommitTrends(
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        List<CommitTrendDTO> trends = statisticsService.getCommitTrends(startDate, endDate);
        return ResponseEntity.ok(trends);
    }

    @PostMapping("/collect")
    public ResponseEntity<String> collectData() {
        try {
            dataCollectionService.collectGitLabData();
            return ResponseEntity.ok("Data collection completed successfully");
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Error collecting data: " + e.getMessage());
        }
    }

    @PostMapping("/collect/project/{projectId}")
    public ResponseEntity<String> collectProjectData(@PathVariable Integer projectId) {
        try {
            dataCollectionService.collectProjectData(projectId.longValue());
            return ResponseEntity.ok("Project data collection completed successfully");
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Error collecting project data: " + e.getMessage());
        }
    }
}
