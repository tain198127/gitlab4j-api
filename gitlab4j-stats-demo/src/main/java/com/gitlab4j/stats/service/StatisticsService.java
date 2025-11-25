package com.gitlab4j.stats.service;

import com.gitlab4j.stats.dto.*;
import com.gitlab4j.stats.entity.DeveloperStats;
import com.gitlab4j.stats.entity.DailyStats;
import com.gitlab4j.stats.entity.ProjectStats;
import com.gitlab4j.stats.repository.CommitDetailRepository;
import com.gitlab4j.stats.repository.DeveloperStatsRepository;
import com.gitlab4j.stats.repository.DailyStatsRepository;
import com.gitlab4j.stats.repository.ProjectStatsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class StatisticsService {

    @Autowired
    private DeveloperStatsRepository developerStatsRepository;

    @Autowired
    private DailyStatsRepository dailyStatsRepository;

    @Autowired
    private ProjectStatsRepository projectStatsRepository;

    @Autowired
    private CommitDetailRepository commitDetailRepository;

    public DeveloperStatsDTO getDeveloperStats(String developerEmail) {
        DeveloperStats stats = developerStatsRepository.findByDeveloperEmail(developerEmail)
                .orElse(null);
        
        if (stats == null) {
            return null;
        }
        
        return new DeveloperStatsDTO(
                stats.getDeveloperName(),
                stats.getDeveloperEmail(),
                stats.getTotalCommits(),
                stats.getTotalLinesAdded(),
                stats.getTotalLinesDeleted(),
                stats.getTotalLinesChanged(),
                stats.getFirstCommitDate(),
                stats.getLastCommitDate()
        );
    }

    public List<DeveloperStatsDTO> getAllDeveloperStats() {
        return developerStatsRepository.findAllByOrderByTotalLinesChangedDesc().stream()
                .map(stats -> new DeveloperStatsDTO(
                        stats.getDeveloperName(),
                        stats.getDeveloperEmail(),
                        stats.getTotalCommits(),
                        stats.getTotalLinesAdded(),
                        stats.getTotalLinesDeleted(),
                        stats.getTotalLinesChanged(),
                        stats.getFirstCommitDate(),
                        stats.getLastCommitDate()
                ))
                .collect(Collectors.toList());
    }

    public List<DeveloperStatsDTO> getTopDevelopersByLinesChanged(int limit) {
        return developerStatsRepository.findAllByOrderByTotalLinesChangedDesc().stream()
                .limit(limit)
                .map(stats -> new DeveloperStatsDTO(
                        stats.getDeveloperName(),
                        stats.getDeveloperEmail(),
                        stats.getTotalCommits(),
                        stats.getTotalLinesAdded(),
                        stats.getTotalLinesDeleted(),
                        stats.getTotalLinesChanged(),
                        stats.getFirstCommitDate(),
                        stats.getLastCommitDate()
                ))
                .collect(Collectors.toList());
    }

    public List<DeveloperStatsDTO> getTopDevelopersByCommits(int limit) {
        return developerStatsRepository.findAllByOrderByTotalCommitsDesc().stream()
                .limit(limit)
                .map(stats -> new DeveloperStatsDTO(
                        stats.getDeveloperName(),
                        stats.getDeveloperEmail(),
                        stats.getTotalCommits(),
                        stats.getTotalLinesAdded(),
                        stats.getTotalLinesDeleted(),
                        stats.getTotalLinesChanged(),
                        stats.getFirstCommitDate(),
                        stats.getLastCommitDate()
                ))
                .collect(Collectors.toList());
    }

    public List<DailyStatsDTO> getDailyStats(LocalDate date) {
        return dailyStatsRepository.findByStatDateOrderByLinesChangedDesc(date).stream()
                .map(stats -> new DailyStatsDTO(
                        stats.getStatDate(),
                        stats.getDeveloperName(),
                        stats.getDeveloperEmail(),
                        stats.getCommitsCount(),
                        stats.getLinesAdded(),
                        stats.getLinesDeleted(),
                        stats.getLinesChanged()
                ))
                .collect(Collectors.toList());
    }

    public List<DailyStatsDTO> getDailyStatsByDeveloper(String developerEmail) {
        return dailyStatsRepository.findByDeveloperEmailOrderByStatDateDesc(developerEmail).stream()
                .map(stats -> new DailyStatsDTO(
                        stats.getStatDate(),
                        stats.getDeveloperName(),
                        stats.getDeveloperEmail(),
                        stats.getCommitsCount(),
                        stats.getLinesAdded(),
                        stats.getLinesDeleted(),
                        stats.getLinesChanged()
                ))
                .collect(Collectors.toList());
    }

    public List<DailyStatsDTO> getDailyStatsByDateRange(LocalDate startDate, LocalDate endDate) {
        return dailyStatsRepository.findByStatDateBetweenOrderByStatDateDescLinesChangedDesc(startDate, endDate).stream()
                .map(stats -> new DailyStatsDTO(
                        stats.getStatDate(),
                        stats.getDeveloperName(),
                        stats.getDeveloperEmail(),
                        stats.getCommitsCount(),
                        stats.getLinesAdded(),
                        stats.getLinesDeleted(),
                        stats.getLinesChanged()
                ))
                .collect(Collectors.toList());
    }

    public ProjectStatsDTO getProjectStats(Long projectId) {
        ProjectStats stats = projectStatsRepository.findByProjectId(projectId)
                .orElse(null);
        
        if (stats == null) {
            return null;
        }
        
        return new ProjectStatsDTO(
                stats.getProjectId(),
                stats.getProjectName(),
                stats.getTotalCommits(),
                stats.getTotalLinesAdded(),
                stats.getTotalLinesDeleted(),
                stats.getTotalLinesChanged(),
                stats.getTotalDevelopers(),
                stats.getFirstCommitDate(),
                stats.getLastCommitDate()
        );
    }

    public SummaryStatsDTO getSummaryStats() {
        Integer totalLinesChanged = developerStatsRepository.getTotalLinesChanged();
        Integer totalCommits = developerStatsRepository.getTotalCommits();
        Integer totalDevelopers = developerStatsRepository.findAll().size();
        Integer totalProjects = projectStatsRepository.findAll().size();
        
        return new SummaryStatsDTO(
                totalLinesChanged != null ? totalLinesChanged : 0,
                totalCommits != null ? totalCommits : 0,
                totalDevelopers,
                totalProjects
        );
    }

    public List<DeveloperActivityDTO> getDeveloperActivity(String developerEmail, LocalDateTime startDate, LocalDateTime endDate) {
        return commitDetailRepository.findByAuthorEmailAndCommitDateBetweenOrderByCommitDateDesc(
                developerEmail, startDate, endDate).stream()
                .map(commit -> new DeveloperActivityDTO(
                        commit.getCommitSha(),
                        commit.getCommitDate(),
                        commit.getMessage(),
                        commit.getLinesAdded(),
                        commit.getLinesDeleted(),
                        commit.getLinesChanged(),
                        commit.getFilesChanged()
                ))
                .collect(Collectors.toList());
    }

    public List<CommitTrendDTO> getCommitTrend(LocalDate startDate, LocalDate endDate) {
        return dailyStatsRepository.findByStatDateBetweenOrderByStatDateDescLinesChangedDesc(startDate, endDate).stream()
                .collect(Collectors.groupingBy(DailyStats::getStatDate))
                .entrySet().stream()
                .map(entry -> {
                    LocalDate date = entry.getKey();
                    List<DailyStats> dailyStats = entry.getValue();
                    
                    int totalCommits = dailyStats.stream().mapToInt(DailyStats::getCommitsCount).sum();
                    int totalLinesChanged = dailyStats.stream().mapToInt(DailyStats::getLinesChanged).sum();
                    int totalDevelopers = dailyStats.size();
                    
                    return new CommitTrendDTO(date, totalCommits, totalLinesChanged, totalDevelopers);
                })
                .sorted((a, b) -> a.getDate().compareTo(b.getDate()))
                .collect(Collectors.toList());
    }
}