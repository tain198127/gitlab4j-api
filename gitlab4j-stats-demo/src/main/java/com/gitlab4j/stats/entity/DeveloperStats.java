package com.gitlab4j.stats.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "developer_stats")
public class DeveloperStats {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "developer_name", nullable = false)
    private String developerName;

    @Column(name = "developer_email", nullable = false)
    private String developerEmail;

    @Column(name = "total_commits", nullable = false)
    private Integer totalCommits = 0;

    @Column(name = "total_lines_added", nullable = false)
    private Integer totalLinesAdded = 0;

    @Column(name = "total_lines_deleted", nullable = false)
    private Integer totalLinesDeleted = 0;

    @Column(name = "total_lines_changed", nullable = false)
    private Integer totalLinesChanged = 0;

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

    public DeveloperStats() {}

    public DeveloperStats(String developerName, String developerEmail) {
        this.developerName = developerName;
        this.developerEmail = developerEmail;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDeveloperName() {
        return developerName;
    }

    public void setDeveloperName(String developerName) {
        this.developerName = developerName;
    }

    public String getDeveloperEmail() {
        return developerEmail;
    }

    public void setDeveloperEmail(String developerEmail) {
        this.developerEmail = developerEmail;
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