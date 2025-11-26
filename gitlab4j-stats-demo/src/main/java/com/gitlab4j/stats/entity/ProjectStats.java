package com.gitlab4j.stats.entity;

import java.time.LocalDateTime;

import javax.persistence.*;

@Entity
@Table(name = "project_stats")
public class ProjectStats {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "project_id", nullable = false)
    private Long projectId;

    @Column(name = "project_name", nullable = false)
    private String projectName;

    @Column(name = "total_commits", nullable = false)
    private Integer totalCommits = 0;

    @Column(name = "total_lines_added", nullable = false)
    private Integer totalLinesAdded = 0;

    @Column(name = "total_lines_deleted", nullable = false)
    private Integer totalLinesDeleted = 0;

    @Column(name = "total_lines_changed", nullable = false)
    private Integer totalLinesChanged = 0;

    @Column(name = "total_developers", nullable = false)
    private Integer totalDevelopers = 0;

    @Column(name = "first_commit_date")
    private LocalDateTime firstCommitDate;

    @Column(name = "last_commit_date")
    private LocalDateTime lastCommitDate;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }

    public ProjectStats() {}

    public ProjectStats(Long projectId, String projectName) {
        this.projectId = projectId;
        this.projectName = projectName;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getProjectId() {
        return projectId;
    }

    public void setProjectId(Long projectId) {
        this.projectId = projectId;
    }

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public Integer getTotalCommits() {
        return totalCommits;
    }

    public void setTotalCommits(Integer totalCommits) {
        this.totalCommits = totalCommits;
    }

    public Integer getTotalLinesAdded() {
        return totalLinesAdded;
    }

    public void setTotalLinesAdded(Integer totalLinesAdded) {
        this.totalLinesAdded = totalLinesAdded;
    }

    public Integer getTotalLinesDeleted() {
        return totalLinesDeleted;
    }

    public void setTotalLinesDeleted(Integer totalLinesDeleted) {
        this.totalLinesDeleted = totalLinesDeleted;
    }

    public Integer getTotalLinesChanged() {
        return totalLinesChanged;
    }

    public void setTotalLinesChanged(Integer totalLinesChanged) {
        this.totalLinesChanged = totalLinesChanged;
    }

    public Integer getTotalDevelopers() {
        return totalDevelopers;
    }

    public void setTotalDevelopers(Integer totalDevelopers) {
        this.totalDevelopers = totalDevelopers;
    }

    public LocalDateTime getFirstCommitDate() {
        return firstCommitDate;
    }

    public void setFirstCommitDate(LocalDateTime firstCommitDate) {
        this.firstCommitDate = firstCommitDate;
    }

    public LocalDateTime getLastCommitDate() {
        return lastCommitDate;
    }

    public void setLastCommitDate(LocalDateTime lastCommitDate) {
        this.lastCommitDate = lastCommitDate;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }
}
