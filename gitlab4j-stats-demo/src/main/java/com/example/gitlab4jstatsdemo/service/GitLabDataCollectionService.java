package com.example.gitlab4jstatsdemo.service;

import com.example.gitlab4jstatsdemo.entity.CommitDetail;
import com.example.gitlab4jstatsdemo.entity.DailyStats;
import com.example.gitlab4jstatsdemo.entity.DeveloperStats;
import com.example.gitlab4jstatsdemo.entity.ProjectStats;
import com.example.gitlab4jstatsdemo.repository.CommitDetailRepository;
import com.example.gitlab4jstatsdemo.repository.DailyStatsRepository;
import com.example.gitlab4jstatsdemo.repository.DeveloperStatsRepository;
import com.example.gitlab4jstatsdemo.repository.ProjectStatsRepository;
import org.gitlab4j.api.GitLabApi;
import org.gitlab4j.api.GitLabApiException;
import org.gitlab4j.api.models.Commit;
import org.gitlab4j.api.models.Diff;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
@Transactional
public class GitLabDataCollectionService {
    private static final Logger logger = LoggerFactory.getLogger(GitLabDataCollectionService.class);
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    private final GitLabApi gitLabApi;
    private final Long projectId;

    private final CommitDetailRepository commitDetailRepository;
    private final DeveloperStatsRepository developerStatsRepository;
    private final DailyStatsRepository dailyStatsRepository;
    private final ProjectStatsRepository projectStatsRepository;

    public GitLabDataCollectionService(
            @Value("${gitlab.api.url}") String gitLabApiUrl,
            @Value("${gitlab.api.token}") String gitLabApiToken,
            @Value("${gitlab.api.project-id}") Long projectId,
            CommitDetailRepository commitDetailRepository,
            DeveloperStatsRepository developerStatsRepository,
            DailyStatsRepository dailyStatsRepository,
            ProjectStatsRepository projectStatsRepository) {
        this.gitLabApi = new GitLabApi(gitLabApiUrl, gitLabApiToken);
        this.projectId = projectId;
        this.commitDetailRepository = commitDetailRepository;
        this.developerStatsRepository = developerStatsRepository;
        this.dailyStatsRepository = dailyStatsRepository;
        this.projectStatsRepository = projectStatsRepository;
    }

    public void collectCommitData() {
        logger.info("Starting to collect commit data for project {}", projectId);

        try {
            List<Commit> commits = gitLabApi.getCommitsApi().getCommits(projectId);
            logger.info("Found {} commits", commits.size());

            for (Commit commit : commits) {
                processCommit(commit);
            }

            logger.info("Commit data collection completed");
        } catch (GitLabApiException e) {
            logger.error("Error collecting commit data for project {}: {}", projectId, e.toString(), e);
            throw new RuntimeException("Failed to collect commit data: " + e.toString(), e);
        }
    }

    private void processCommit(Commit commit) throws GitLabApiException {
        // Check if commit is already processed
        if (commitDetailRepository.findByCommitId(commit.getId()).isPresent()) {
            logger.debug("Commit {} already processed, skipping", commit.getId());
            return;
        }

        // Get diff for the commit
        List<Diff> diffs = gitLabApi.getCommitsApi().getDiff(projectId, commit.getId());

        // Calculate total additions and deletions
        long additions = diffs.stream()
            .mapToLong(diff -> diff.getDiff().lines()
                .filter(line -> line.startsWith("+"))
                .count())
            .sum();
        long deletions = diffs.stream()
            .mapToLong(diff -> diff.getDiff().lines()
                .filter(line -> line.startsWith("-"))
                .count())
            .sum();

        // Save commit detail
        CommitDetail commitDetail = new CommitDetail();
        commitDetail.setCommitId(commit.getId());
        commitDetail.setMessage(commit.getMessage());
        commitDetail.setTimestamp(commit.getCommittedDate().getTime());
        commitDetail.setUsername(commit.getAuthorName());
        commitDetail.setName(commit.getAuthorName());
        commitDetail.setEmail(commit.getAuthorEmail());
        commitDetail.setAdditions(additions);
        commitDetail.setDeletions(deletions);
        commitDetailRepository.save(commitDetail);

        // Update developer stats
        updateDeveloperStats(commit, additions, deletions);

        // Update daily stats
        updateDailyStats(commit, additions, deletions);

        // Update project stats
        updateProjectStats(additions, deletions);

        logger.debug("Processed commit {}: +{} -{} by {}",
                commit.getId(), additions, deletions, commit.getAuthorName());
    }

    private void updateDeveloperStats(Commit commit, long additions, long deletions) {
        String username = commit.getAuthorName();
        DeveloperStats developerStats = developerStatsRepository.findByUsername(username)
                .orElseGet(() -> {
                    DeveloperStats newStats = new DeveloperStats();
                    newStats.setUsername(username);
                    newStats.setName(commit.getAuthorName());
                    newStats.setEmail(commit.getAuthorEmail());
                    newStats.setFirstCommitTimestamp(commit.getCommittedDate().getTime());
                    return newStats;
                });

        developerStats.setTotalAdditions(developerStats.getTotalAdditions() + additions);
        developerStats.setTotalDeletions(developerStats.getTotalDeletions() + deletions);
        developerStats.setTotalCommits(developerStats.getTotalCommits() + 1);
        developerStats.setLastCommitTimestamp(commit.getCommittedDate().getTime());

        developerStatsRepository.save(developerStats);
    }

    private void updateDailyStats(Commit commit, long additions, long deletions) {
        String date = LocalDate.ofInstant(commit.getCommittedDate().toInstant(), ZoneId.systemDefault())
                .format(DATE_FORMATTER);
        String username = commit.getAuthorName();

        List<DailyStats> dailyStatsList = dailyStatsRepository.findByUsername(username);
        DailyStats dailyStats = dailyStatsList.stream()
                .filter(ds -> ds.getDate().equals(date))
                .findFirst()
                .orElseGet(() -> {
                    DailyStats newStats = new DailyStats();
                    newStats.setDate(date);
                    newStats.setUsername(username);
                    return newStats;
                });

        dailyStats.setAdditions(dailyStats.getAdditions() + additions);
        dailyStats.setDeletions(dailyStats.getDeletions() + deletions);
        dailyStats.setCommits(dailyStats.getCommits() + 1);

        dailyStatsRepository.save(dailyStats);
    }

    private void updateProjectStats(long additions, long deletions) {
        ProjectStats projectStats = projectStatsRepository.findByProjectId(projectId)
                .orElseGet(() -> {
                    ProjectStats newStats = new ProjectStats();
                    newStats.setProjectId(projectId);
                    return newStats;
                });

        projectStats.setTotalAdditions(projectStats.getTotalAdditions() + additions);
        projectStats.setTotalDeletions(projectStats.getTotalDeletions() + deletions);
        projectStats.setTotalCommits(projectStats.getTotalCommits() + 1);

        projectStatsRepository.save(projectStats);
    }
}