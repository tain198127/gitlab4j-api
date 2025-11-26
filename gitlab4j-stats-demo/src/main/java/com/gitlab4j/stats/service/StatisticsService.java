package com.gitlab4j.stats.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gitlab4j.stats.dto.*;
import com.gitlab4j.stats.entity.CommitDetail;
import com.gitlab4j.stats.entity.DailyStats;
import com.gitlab4j.stats.entity.DeveloperStats;
import com.gitlab4j.stats.entity.ProjectStats;
import com.gitlab4j.stats.repository.CommitDetailRepository;
import com.gitlab4j.stats.repository.DailyStatsRepository;
import com.gitlab4j.stats.repository.DeveloperStatsRepository;
import com.gitlab4j.stats.repository.ProjectStatsRepository;

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
        DeveloperStats stats =
                developerStatsRepository.findByDeveloperEmail(developerEmail).orElse(null);

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
                stats.getLastCommitDate());
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
                        stats.getLastCommitDate()))
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
                        stats.getLastCommitDate()))
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
                        stats.getLastCommitDate()))
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
                        stats.getLinesChanged()))
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
                        stats.getLinesChanged()))
                .collect(Collectors.toList());
    }

    public List<DailyStatsDTO> getDailyStatsByDateRange(LocalDate startDate, LocalDate endDate) {
        return dailyStatsRepository
                .findByStatDateBetweenOrderByStatDateDescLinesChangedDesc(startDate, endDate)
                .stream()
                .map(stats -> new DailyStatsDTO(
                        stats.getStatDate(),
                        stats.getDeveloperName(),
                        stats.getDeveloperEmail(),
                        stats.getCommitsCount(),
                        stats.getLinesAdded(),
                        stats.getLinesDeleted(),
                        stats.getLinesChanged()))
                .collect(Collectors.toList());
    }

    public ProjectStatsDTO getProjectStats(Long projectId) {
        ProjectStats stats = projectStatsRepository.findByProjectId(projectId).orElse(null);

        if (stats == null) {
            return null;
        }

        return new ProjectStatsDTO(
                stats.getProjectId() != null ? stats.getProjectId().intValue() : null,
                stats.getProjectName(),
                stats.getTotalCommits(),
                stats.getTotalLinesAdded(),
                stats.getTotalLinesDeleted(),
                stats.getTotalLinesChanged(),
                stats.getTotalDevelopers());
    }

    public SummaryStatsDTO getSummaryStats() {
        Integer totalLinesChanged = developerStatsRepository.getTotalLinesChanged();
        Integer totalCommits = developerStatsRepository.getTotalCommits();
        Integer totalLinesAdded = developerStatsRepository.getTotalLinesAdded();
        Integer totalLinesDeleted = developerStatsRepository.getTotalLinesDeleted();
        Integer totalDevelopers = developerStatsRepository.findAll().size();
        Integer totalProjects = projectStatsRepository.findAll().size();

        return new SummaryStatsDTO(
                totalDevelopers,
                totalCommits != null ? totalCommits : 0,
                totalLinesAdded != null ? totalLinesAdded : 0,
                totalLinesDeleted != null ? totalLinesDeleted : 0,
                totalLinesChanged != null ? totalLinesChanged : 0,
                totalProjects);
    }

    public List<DeveloperActivityDTO> getDeveloperActivity(
            String developerEmail, LocalDateTime startDate, LocalDateTime endDate) {
        return commitDetailRepository
                .findByAuthorEmailAndCommitDateBetweenOrderByCommitDateDesc(developerEmail, startDate, endDate)
                .stream()
                .collect(Collectors.groupingBy(c -> c.getAuthorEmail()))
                .entrySet()
                .stream()
                .map(entry -> {
                    List<CommitDetail> commits = entry.getValue();
                    int totalCommits = commits.size();
                    int totalLinesChanged = commits.stream()
                            .mapToInt(c -> c.getLinesChanged() != null ? c.getLinesChanged() : 0)
                            .sum();
                    double avgLines = totalCommits > 0 ? (double) totalLinesChanged / totalCommits : 0.0;

                    CommitDetail firstCommit = commits.get(0);
                    return new DeveloperActivityDTO(
                            firstCommit.getAuthorName(),
                            firstCommit.getAuthorEmail(),
                            totalCommits,
                            totalLinesChanged,
                            avgLines);
                })
                .collect(Collectors.toList());
    }

    public List<CommitTrendDTO> getCommitTrend(LocalDate startDate, LocalDate endDate) {
        return dailyStatsRepository
                .findByStatDateBetweenOrderByStatDateDescLinesChangedDesc(startDate, endDate)
                .stream()
                .collect(Collectors.groupingBy(DailyStats::getStatDate))
                .entrySet()
                .stream()
                .map(entry -> {
                    LocalDate date = entry.getKey();
                    List<DailyStats> dailyStats = entry.getValue();

                    int totalCommits = dailyStats.stream()
                            .mapToInt(DailyStats::getCommitsCount)
                            .sum();
                    int totalLinesAdded = dailyStats.stream()
                            .mapToInt(DailyStats::getLinesAdded)
                            .sum();
                    int totalLinesDeleted = dailyStats.stream()
                            .mapToInt(DailyStats::getLinesDeleted)
                            .sum();
                    int totalLinesChanged = dailyStats.stream()
                            .mapToInt(DailyStats::getLinesChanged)
                            .sum();

                    return new CommitTrendDTO(
                            date, totalCommits, totalLinesAdded, totalLinesDeleted, totalLinesChanged);
                })
                .sorted((a, b) -> a.getDate().compareTo(b.getDate()))
                .collect(Collectors.toList());
    }

    // Additional methods needed by StatisticsController
    public List<DeveloperStatsDTO> getDeveloperStats(int limit, String sortBy) {
        if ("commits".equals(sortBy)) {
            return getTopDevelopersByCommits(limit);
        }
        return getTopDevelopersByLinesChanged(limit);
    }

    public DeveloperStatsDTO getDeveloperStatsByEmail(String email) {
        return getDeveloperStats(email);
    }

    public List<DailyStatsDTO> getDailyStats(LocalDate startDate, LocalDate endDate, String developerEmail) {
        if (developerEmail != null && !developerEmail.isEmpty()) {
            return getDailyStatsByDeveloper(developerEmail);
        }
        if (startDate != null && endDate != null) {
            return getDailyStatsByDateRange(startDate, endDate);
        }
        if (startDate != null) {
            return getDailyStats(startDate);
        }
        return getAllDeveloperStats().stream()
                .flatMap(dev -> getDailyStatsByDeveloper(dev.getDeveloperEmail()).stream())
                .collect(Collectors.toList());
    }

    public List<ProjectStatsDTO> getProjectStats() {
        return projectStatsRepository.findAll().stream()
                .map(stats -> new ProjectStatsDTO(
                        stats.getProjectId() != null ? stats.getProjectId().intValue() : null,
                        stats.getProjectName(),
                        stats.getTotalCommits(),
                        stats.getTotalLinesAdded(),
                        stats.getTotalLinesDeleted(),
                        stats.getTotalLinesChanged(),
                        stats.getTotalDevelopers()))
                .collect(Collectors.toList());
    }

    public ProjectStatsDTO getProjectStatsById(Integer projectId) {
        return getProjectStats(projectId != null ? projectId.longValue() : null);
    }

    public List<DeveloperActivityDTO> getDeveloperActivity(LocalDate startDate, LocalDate endDate) {
        LocalDateTime startDateTime = startDate != null ? startDate.atStartOfDay() : LocalDateTime.now().minusDays(30);
        LocalDateTime endDateTime = endDate != null ? endDate.atTime(23, 59, 59) : LocalDateTime.now();
        
        return commitDetailRepository.findAll().stream()
                .filter(commit -> {
                    LocalDateTime commitDate = commit.getCommitDate();
                    return commitDate != null && 
                           !commitDate.isBefore(startDateTime) && 
                           !commitDate.isAfter(endDateTime);
                })
                .collect(Collectors.groupingBy(c -> c.getAuthorEmail()))
                .entrySet()
                .stream()
                .map(entry -> {
                    List<CommitDetail> commits = entry.getValue();
                    int totalCommits = commits.size();
                    int totalLinesChanged = commits.stream()
                            .mapToInt(c -> c.getLinesChanged() != null ? c.getLinesChanged() : 0)
                            .sum();
                    double avgLines = totalCommits > 0 ? (double) totalLinesChanged / totalCommits : 0.0;

                    CommitDetail firstCommit = commits.get(0);
                    return new DeveloperActivityDTO(
                            firstCommit.getAuthorName(),
                            firstCommit.getAuthorEmail(),
                            totalCommits,
                            totalLinesChanged,
                            avgLines);
                })
                .collect(Collectors.toList());
    }

    public List<CommitTrendDTO> getCommitTrends(LocalDate startDate, LocalDate endDate) {
        if (startDate == null) {
            startDate = LocalDate.now().minusDays(30);
        }
        if (endDate == null) {
            endDate = LocalDate.now();
        }
        return getCommitTrend(startDate, endDate);
    }
}