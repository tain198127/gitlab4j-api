package com.gitlab4j.stats.service;

import com.gitlab4j.stats.entity.CommitDetail;
import com.gitlab4j.stats.entity.DeveloperStats;
import com.gitlab4j.stats.entity.DailyStats;
import com.gitlab4j.stats.entity.ProjectStats;
import com.gitlab4j.stats.repository.CommitDetailRepository;
import com.gitlab4j.stats.repository.DeveloperStatsRepository;
import com.gitlab4j.stats.repository.DailyStatsRepository;
import com.gitlab4j.stats.repository.ProjectStatsRepository;
import org.gitlab4j.api.GitLabApi;
import org.gitlab4j.api.models.Commit;
import org.gitlab4j.api.models.Diff;
import org.gitlab4j.api.models.Project;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

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
            logger.info("Starting GitLab data collection for project ID: {}", projectId);
            
            Project project = gitLabApi.getProjectApi().getProject(projectId);
            logger.info("Found project: {} ({})", project.getName(), project.getId());
            
            updateProjectStats(project);
            collectCommitData(project);
            
            logger.info("GitLab data collection completed successfully");
        } catch (Exception e) {
            logger.error("Error collecting GitLab data", e);
        }
    }

    private void updateProjectStats(Project project) {
        Optional<ProjectStats> existingStats = projectStatsRepository.findByProjectId(project.getId());
        ProjectStats stats = existingStats.orElse(new ProjectStats(project.getId(), project.getName()));
        
        stats.setTotalCommits(commitDetailRepository.findByProjectIdOrderByCommitDateDesc(project.getId()).size());
        stats.setTotalLinesChanged(commitDetailRepository.findByProjectIdOrderByCommitDateDesc(project.getId())
                .stream()
                .mapToInt(CommitDetail::getLinesChanged)
                .sum());
        stats.setTotalLinesAdded(commitDetailRepository.findByProjectIdOrderByCommitDateDesc(project.getId())
                .stream()
                .mapToInt(CommitDetail::getLinesAdded)
                .sum());
        stats.setTotalLinesDeleted(commitDetailRepository.findByProjectIdOrderByCommitDateDesc(project.getId())
                .stream()
                .mapToInt(CommitDetail::getLinesDeleted)
                .sum());
        stats.setTotalDevelopers((int) commitDetailRepository.findByProjectIdOrderByCommitDateDesc(project.getId())
                .stream()
                .map(CommitDetail::getAuthorEmail)
                .distinct()
                .count());
        
        if (!commitDetailRepository.findByProjectIdOrderByCommitDateDesc(project.getId()).isEmpty()) {
            CommitDetail firstCommit = commitDetailRepository.findByProjectIdOrderByCommitDateDesc(project.getId())
                    .stream()
                    .min((a, b) -> a.getCommitDate().compareTo(b.getCommitDate()))
                    .orElse(null);
            CommitDetail lastCommit = commitDetailRepository.findByProjectIdOrderByCommitDateDesc(project.getId())
                    .stream()
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
            List<Commit> commits = gitLabApi.getCommitsApi().getCommits(project.getId());
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
            List<Diff> diffs = gitLabApi.getCommitsApi().getDiff(project.getId(), commitSha);
            
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
            
            CommitDetail commitDetail = new CommitDetail(
                    commitSha,
                    project.getId(),
                    project.getName(),
                    commit.getAuthorName(),
                    commit.getAuthorEmail(),
                    LocalDateTime.parse(commit.getCreatedAt(), DateTimeFormatter.ISO_DATE_TIME)
            );
            
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
        Optional<DeveloperStats> existingStats = developerStatsRepository.findByDeveloperEmail(commitDetail.getAuthorEmail());
        DeveloperStats stats = existingStats.orElse(new DeveloperStats(
                commitDetail.getAuthorName(), 
                commitDetail.getAuthorEmail()
        ));
        
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
        
        Optional<DailyStats> existingStats = dailyStatsRepository.findByStatDateAndDeveloperEmail(
                commitDate, commitDetail.getAuthorEmail());
        DailyStats stats = existingStats.orElse(new DailyStats(
                commitDate,
                commitDetail.getAuthorName(),
                commitDetail.getAuthorEmail()
        ));
        
        stats.setCommitsCount(stats.getCommitsCount() + 1);
        stats.setLinesAdded(stats.getLinesAdded() + commitDetail.getLinesAdded());
        stats.setLinesDeleted(stats.getLinesDeleted() + commitDetail.getLinesDeleted());
        stats.setLinesChanged(stats.getLinesChanged() + commitDetail.getLinesChanged());
        
        dailyStatsRepository.save(stats);
        logger.debug("Updated daily stats for {}: {}", commitDetail.getAuthorEmail(), commitDate);
    }
}