package com.gitlab4j.stats.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.gitlab4j.api.GitLabApi;
import org.gitlab4j.api.models.Commit;
import org.gitlab4j.api.models.Diff;
import org.gitlab4j.api.models.Project;
import org.gitlab4j.api.models.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.gitlab4j.stats.entity.CommitDetail;
import com.gitlab4j.stats.entity.DailyStats;
import com.gitlab4j.stats.entity.DeveloperStats;
import com.gitlab4j.stats.entity.ProjectStats;
import com.gitlab4j.stats.repository.CommitDetailRepository;
import com.gitlab4j.stats.repository.DailyStatsRepository;
import com.gitlab4j.stats.repository.DeveloperStatsRepository;
import com.gitlab4j.stats.repository.ProjectStatsRepository;

@Service
public class GitLabDataCollectionService {

    private static final Logger logger = LoggerFactory.getLogger(GitLabDataCollectionService.class);

    @Autowired
    private GitLabApi gitLabApi;

    @Autowired
    private CommitDetailRepository commitDetailRepository;

    @Autowired
    private DeveloperStatsRepository developerStatsRepository;

    @Autowired
    private DailyStatsRepository dailyStatsRepository;

    @Autowired
    private ProjectStatsRepository projectStatsRepository;

    @Value("${gitlab.project.id}")
    private Long projectId;

    @Async
    @Transactional
    public void collectGitLabData() {
        try {
            // Skip data collection if project ID is not configured
            if (projectId == null || projectId <= 0) {
                logger.warn("GitLab project ID is not configured. Skipping data collection.");
                return;
            }
            
            logger.info("Starting GitLab data collection for project ID: {}", projectId);
            
            // Debug logging
            if (projectId == null) {
                logger.error("Project ID is null!");
                return;
            }
            logger.debug("Using project ID: {}", projectId);

            // Use project ID as Integer to match GitLab4J API expectations
            Integer projectIdInt = projectId.intValue();
            logger.debug("Attempting to get project with ID: {} (type: {})", projectIdInt, projectIdInt.getClass().getSimpleName());
            
            // Test GitLab API connection by getting current user
            try {
                logger.info("Testing GitLab API connection...");
                User currentUser = gitLabApi.getUserApi().getCurrentUser();
                logger.info("GitLab API connection successful. Current user: {}", currentUser.getUsername());
            } catch (Exception e) {
                logger.error("GitLab API authentication failed: {}", e.getMessage());
                logger.warn("Continuing with data collection anyway. If the project is public, this might still work.");
                // Don't throw exception, continue with project retrieval which might work for public projects
            }
            
            Project project;
            try {
                project = gitLabApi.getProjectApi().getProject(projectIdInt);
                logger.info("Successfully retrieved project: {} (ID: {})", project.getName(), project.getId());
            } catch (Exception e) {
                logger.error("Failed to retrieve project with ID: {}. Error: {}", projectIdInt, e.getMessage());
                logger.error("This could mean:\n" +
                            "1. The project ID is incorrect\n" +
                            "2. The project is private and your token doesn't have access\n" +
                            "3. Your GitLab API token is invalid or expired\n" +
                            "4. The GitLab API URL is incorrect\n" +
                            "Please check your configuration in application.properties");
                return;
            }
            logger.info("Found project: {} ({})", project.getName(), project.getId());

            updateProjectStats(project);
            collectCommitData(project);

            logger.info("GitLab data collection completed successfully");
        } catch (Exception e) {
            logger.error("Error collecting GitLab data", e);
        }
    }

    private void updateProjectStats(Project project) {
        Optional<ProjectStats> existingStats = projectStatsRepository.findByProjectId(project.getId().longValue());
        ProjectStats stats = existingStats.orElse(new ProjectStats(project.getId().longValue(), project.getName()));

        stats.setTotalCommits(commitDetailRepository.findByProjectIdOrderByCommitDateDesc(project.getId().longValue()).size());
        stats.setTotalLinesChanged(commitDetailRepository.findByProjectIdOrderByCommitDateDesc(project.getId().longValue()).stream()
                .mapToInt(CommitDetail::getLinesChanged)
                .sum());
        stats.setTotalLinesAdded(commitDetailRepository.findByProjectIdOrderByCommitDateDesc(project.getId().longValue()).stream()
                .mapToInt(CommitDetail::getLinesAdded)
                .sum());
        stats.setTotalLinesDeleted(commitDetailRepository.findByProjectIdOrderByCommitDateDesc(project.getId().longValue()).stream()
                .mapToInt(CommitDetail::getLinesDeleted)
                .sum());
        stats.setTotalDevelopers(
                (int) commitDetailRepository.findByProjectIdOrderByCommitDateDesc(project.getId().longValue()).stream()
                        .map(CommitDetail::getAuthorEmail)
                        .distinct()
                        .count());

        if (!commitDetailRepository
                .findByProjectIdOrderByCommitDateDesc(project.getId().longValue())
                .isEmpty()) {
            CommitDetail firstCommit =
                    commitDetailRepository.findByProjectIdOrderByCommitDateDesc(project.getId().longValue()).stream()
                            .min((a, b) -> a.getCommitDate().compareTo(b.getCommitDate()))
                            .orElse(null);
            CommitDetail lastCommit =
                    commitDetailRepository.findByProjectIdOrderByCommitDateDesc(project.getId().longValue()).stream()
                            .max((a, b) -> a.getCommitDate().compareTo(b.getCommitDate()))
                            .orElse(null);

            if (firstCommit != null) {
                stats.setFirstCommitDate(firstCommit.getCommitDate());
            }
            if (lastCommit != null) {
                stats.setLastCommitDate(lastCommit.getCommitDate());
            }
        }

        projectStatsRepository.save(stats);
        logger.info("Updated project stats for project: {}", project.getName());
    }

    private void collectCommitData(Project project) {
        try {
            // Use project ID as Integer to match GitLab4J API expectations
            Integer projectIdInt = project.getId().intValue();
            List<Commit> commits = gitLabApi.getCommitsApi().getCommits(projectIdInt);
            logger.info("Found {} commits for project: {}", commits.size(), project.getName());

            for (Commit commit : commits) {
                try {
                    processCommit(project, commit);
                } catch (Exception e) {
                    logger.error("Error processing commit: {}", commit.getId(), e);
                }
            }
        } catch (Exception e) {
            logger.error("Error collecting commits for project: {}", project.getName(), e);
        }
    }

    private void processCommit(Project project, Commit commit) {
        String commitSha = commit.getId();

        if (commitDetailRepository.findByCommitSha(commitSha).isPresent()) {
            logger.debug("Commit {} already processed, skipping", commitSha);
            return;
        }

        try {
            // Use project ID as Integer to match GitLab4J API expectations
            Integer projectIdInt = project.getId().intValue();
            List<Diff> diffs = gitLabApi.getCommitsApi().getDiff(projectIdInt, commitSha);

            int linesAdded = 0;
            int linesDeleted = 0;
            int filesChanged = diffs.size();

            for (Diff diff : diffs) {
                String diffContent = diff.getDiff();
                if (diffContent != null) {
                    String[] lines = diffContent.split("\n");
                    for (String line : lines) {
                        if (line.startsWith("+") && !line.startsWith("+++")) {
                            linesAdded++;
                        } else if (line.startsWith("-") && !line.startsWith("---")) {
                            linesDeleted++;
                        }
                    }
                }
            }

            // Convert Date to LocalDateTime
            LocalDateTime commitDate = commit.getCreatedAt() != null
                    ? commit.getCreatedAt()
                            .toInstant()
                            .atZone(java.time.ZoneId.systemDefault())
                            .toLocalDateTime()
                    : LocalDateTime.now();

            CommitDetail commitDetail = new CommitDetail(
                    commitSha,
                    project.getId().longValue(),
                    project.getName(),
                    commit.getAuthorName(),
                    commit.getAuthorEmail(),
                    commitDate);

            commitDetail.setMessage(commit.getMessage());
            commitDetail.setLinesAdded(linesAdded);
            commitDetail.setLinesDeleted(linesDeleted);
            commitDetail.setLinesChanged(linesAdded + linesDeleted);
            commitDetail.setFilesChanged(filesChanged);

            commitDetailRepository.save(commitDetail);
            logger.debug("Saved commit detail for commit: {}", commitSha);

            updateDeveloperStats(commitDetail);
            updateDailyStats(commitDetail);

        } catch (Exception e) {
            logger.error("Error processing commit: {}", commitSha, e);
        }
    }

    private void updateDeveloperStats(CommitDetail commitDetail) {
        Optional<DeveloperStats> existingStats =
                developerStatsRepository.findByDeveloperEmail(commitDetail.getAuthorEmail());
        DeveloperStats stats =
                existingStats.orElse(new DeveloperStats(commitDetail.getAuthorName(), commitDetail.getAuthorEmail()));

        stats.setTotalCommits(stats.getTotalCommits() + 1);
        stats.setTotalLinesAdded(stats.getTotalLinesAdded() + commitDetail.getLinesAdded());
        stats.setTotalLinesDeleted(stats.getTotalLinesDeleted() + commitDetail.getLinesDeleted());
        stats.setTotalLinesChanged(stats.getTotalLinesChanged() + commitDetail.getLinesChanged());

        if (stats.getFirstCommitDate() == null || commitDetail.getCommitDate().isBefore(stats.getFirstCommitDate())) {
            stats.setFirstCommitDate(commitDetail.getCommitDate());
        }

        if (stats.getLastCommitDate() == null || commitDetail.getCommitDate().isAfter(stats.getLastCommitDate())) {
            stats.setLastCommitDate(commitDetail.getCommitDate());
        }

        developerStatsRepository.save(stats);
        logger.debug("Updated developer stats for: {}", commitDetail.getAuthorEmail());
    }

    private void updateDailyStats(CommitDetail commitDetail) {
        LocalDate commitDate = commitDetail.getCommitDate().toLocalDate();

        Optional<DailyStats> existingStats =
                dailyStatsRepository.findByStatDateAndDeveloperEmail(commitDate, commitDetail.getAuthorEmail());
        DailyStats stats = existingStats.orElse(
                new DailyStats(commitDate, commitDetail.getAuthorName(), commitDetail.getAuthorEmail()));

        stats.setCommitsCount(stats.getCommitsCount() + 1);
        stats.setLinesAdded(stats.getLinesAdded() + commitDetail.getLinesAdded());
        stats.setLinesDeleted(stats.getLinesDeleted() + commitDetail.getLinesDeleted());
        stats.setLinesChanged(stats.getLinesChanged() + commitDetail.getLinesChanged());

        dailyStatsRepository.save(stats);
        logger.debug("Updated daily stats for {}: {}", commitDetail.getAuthorEmail(), commitDate);
    }

    @Async
    @Transactional
    public void collectProjectData(Long projectId) {
        try {
            logger.info("Starting GitLab data collection for project ID: {}", projectId);
            
            // Debug logging
            if (projectId == null) {
                logger.error("Project ID is null!");
                return;
            }
            logger.debug("Using project ID: {}", projectId);

            // Use project ID as Integer to match GitLab4J API expectations
            Integer projectIdInt = projectId.intValue();
            Project project = gitLabApi.getProjectApi().getProject(projectIdInt);
            logger.info("Found project: {} ({})", project.getName(), project.getId());

            // Collect commits first, then update project stats
            collectCommitData(project);
            updateProjectStats(project);

            logger.info("GitLab data collection completed successfully for project: {}", projectId);
        } catch (Exception e) {
            logger.error("Error collecting GitLab data for project: {}", projectId, e);
        }
    }
}
