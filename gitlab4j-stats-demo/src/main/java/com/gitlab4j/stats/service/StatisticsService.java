package com.gitlab4j.stats.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;
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

    public MultiProjectStatsDTO getMultiProjectStats() {
        List<ProjectStats> allProjects = projectStatsRepository.findAll();
        List<DeveloperStats> allDevelopers = developerStatsRepository.findAll();
        
        int totalProjects = allProjects.size();
        int totalDevelopers = allDevelopers.size();
        int totalCommits = allProjects.stream().mapToInt(ProjectStats::getTotalCommits).sum();
        int totalLinesAdded = allProjects.stream().mapToInt(ProjectStats::getTotalLinesAdded).sum();
        int totalLinesDeleted = allProjects.stream().mapToInt(ProjectStats::getTotalLinesDeleted).sum();
        int totalLinesChanged = allProjects.stream().mapToInt(ProjectStats::getTotalLinesChanged).sum();
        
        // Calculate project-developer distribution
        Map<String, Integer> projectDeveloperDistribution = new HashMap<>();
        List<ProjectDeveloperStatsDTO> projectDeveloperStats = new ArrayList<>();
        
        // Get all commit details to analyze project-developer relationships
        List<CommitDetail> allCommits = commitDetailRepository.findAll();
        
        Map<String, Map<String, List<CommitDetail>>> projectDeveloperCommits = allCommits.stream()
            .collect(Collectors.groupingBy(
                commit -> commitDetailRepository.findById(commit.getId())
                    .map(c -> projectStatsRepository.findByProjectId(c.getProjectId())
                        .map(ProjectStats::getProjectName)
                        .orElse("Unknown Project"))
                    .orElse("Unknown Project"),
                Collectors.groupingBy(CommitDetail::getAuthorEmail)
            ));
        
        for (Map.Entry<String, Map<String, List<CommitDetail>>> projectEntry : projectDeveloperCommits.entrySet()) {
            String projectName = projectEntry.getKey();
            int projectDeveloperCount = projectEntry.getValue().size();
            projectDeveloperDistribution.put(projectName, projectDeveloperCount);
            
            for (Map.Entry<String, List<CommitDetail>> developerEntry : projectEntry.getValue().entrySet()) {
                String developerEmail = developerEntry.getKey();
                List<CommitDetail> commits = developerEntry.getValue();
                
                DeveloperStats developer = allDevelopers.stream()
                    .filter(d -> d.getDeveloperEmail().equals(developerEmail))
                    .findFirst()
                    .orElse(null);
                
                if (developer != null) {
                    int commitsCount = commits.size();
                    int linesAdded = commits.stream().mapToInt(c -> c.getLinesAdded() != null ? c.getLinesAdded() : 0).sum();
                    int linesDeleted = commits.stream().mapToInt(c -> c.getLinesDeleted() != null ? c.getLinesDeleted() : 0).sum();
                    int linesChanged = commits.stream().mapToInt(c -> c.getLinesChanged() != null ? c.getLinesChanged() : 0).sum();
                    
                    // Calculate commit frequency (commits per day)
                    long daysBetween = java.time.temporal.ChronoUnit.DAYS.between(
                        developer.getFirstCommitDate().toLocalDate(),
                        developer.getLastCommitDate().toLocalDate()
                    ) + 1;
                    double commitFrequency = daysBetween > 0 ? (double) commitsCount / daysBetween : 0.0;
                    
                    projectDeveloperStats.add(new ProjectDeveloperStatsDTO(
                        projectName,
                        developer.getDeveloperName(),
                        developerEmail,
                        commitsCount,
                        linesAdded,
                        linesDeleted,
                        linesChanged,
                        commitFrequency,
                        developer.getFirstCommitDate().toLocalDate().toString(),
                        developer.getLastCommitDate().toLocalDate().toString()
                    ));
                }
            }
        }
        
        return new MultiProjectStatsDTO(
            totalProjects,
            totalDevelopers,
            totalCommits,
            totalLinesAdded,
            totalLinesDeleted,
            totalLinesChanged,
            projectDeveloperDistribution,
            projectDeveloperStats
        );
    }

    public DeveloperCrossProjectStatsDTO getDeveloperCrossProjectStats(String developerEmail) {
        DeveloperStats developer = developerStatsRepository.findByDeveloperEmail(developerEmail).orElse(null);
        if (developer == null) {
            return null;
        }
        
        List<CommitDetail> developerCommits = commitDetailRepository.findByAuthorEmailOrderByCommitDateDesc(developerEmail);
        
        // Group commits by project
        Map<Long, List<CommitDetail>> commitsByProject = developerCommits.stream()
            .collect(Collectors.groupingBy(CommitDetail::getProjectId));
        
        int totalProjects = commitsByProject.size();
        int totalCommits = developerCommits.size();
        int totalLinesAdded = developer.getTotalLinesAdded();
        int totalLinesDeleted = developer.getTotalLinesDeleted();
        int totalLinesChanged = developer.getTotalLinesChanged();
        
        double averageCommitsPerProject = totalProjects > 0 ? (double) totalCommits / totalProjects : 0.0;
        double averageLinesPerProject = totalProjects > 0 ? (double) totalLinesChanged / totalProjects : 0.0;
        
        // Calculate project activities
        List<DeveloperProjectActivityDTO> projectActivities = new ArrayList<>();
        Map<String, Integer> projectCommitCounts = new HashMap<>();
        
        for (Map.Entry<Long, List<CommitDetail>> entry : commitsByProject.entrySet()) {
            Long projectId = entry.getKey();
            List<CommitDetail> projectCommits = entry.getValue();
            
            String projectName = projectStatsRepository.findByProjectId(projectId)
                .map(ProjectStats::getProjectName)
                .orElse("Unknown Project");
            
            int projectCommitsCount = projectCommits.size();
            int projectLinesAdded = projectCommits.stream().mapToInt(c -> c.getLinesAdded() != null ? c.getLinesAdded() : 0).sum();
            int projectLinesDeleted = projectCommits.stream().mapToInt(c -> c.getLinesDeleted() != null ? c.getLinesDeleted() : 0).sum();
            int projectLinesChanged = projectCommits.stream().mapToInt(c -> c.getLinesChanged() != null ? c.getLinesChanged() : 0).sum();
            
            projectCommitCounts.put(projectName, projectCommitsCount);
            
            // Calculate activity score based on commits and lines changed
            double activityScore = (projectCommitsCount * 0.6) + (projectLinesChanged * 0.004);
            
            DeveloperProjectActivityDTO activity = new DeveloperProjectActivityDTO(
                projectName,
                projectCommitsCount,
                projectLinesAdded,
                projectLinesDeleted,
                projectLinesChanged,
                projectCommits.get(0).getCommitDate().toLocalDate().toString(),
                projectCommits.get(projectCommits.size() - 1).getCommitDate().toLocalDate().toString(),
                activityScore
            );
            
            projectActivities.add(activity);
        }
        
        // Sort by activity score to find most and least active projects
        projectActivities.sort((a, b) -> Double.compare(b.getActivityScore(), a.getActivityScore()));
        
        String mostActiveProject = projectActivities.isEmpty() ? "None" : projectActivities.get(0).getProjectName();
        String leastActiveProject = projectActivities.isEmpty() ? "None" : 
            projectActivities.get(projectActivities.size() - 1).getProjectName();
        
        // Calculate commit frequency score (0-100)
        long totalDays = java.time.temporal.ChronoUnit.DAYS.between(
            developer.getFirstCommitDate().toLocalDate(),
            developer.getLastCommitDate().toLocalDate()
        ) + 1;
        double commitFrequencyScore = totalDays > 0 ? Math.min(100.0, (totalCommits * 100.0) / totalDays) : 0.0;
        
        return new DeveloperCrossProjectStatsDTO(
            developer.getDeveloperName(),
            developerEmail,
            totalProjects,
            totalCommits,
            totalLinesAdded,
            totalLinesDeleted,
            totalLinesChanged,
            averageCommitsPerProject,
            averageLinesPerProject,
            projectCommitCounts,
            projectActivities,
            mostActiveProject,
            leastActiveProject,
            commitFrequencyScore
        );
    }
}