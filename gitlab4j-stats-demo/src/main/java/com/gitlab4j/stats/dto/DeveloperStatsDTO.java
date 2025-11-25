package com.gitlab4j.stats.dto;

import java.time.LocalDateTime;

public class DeveloperStatsDTO {
    private String developerName;
    private String developerEmail;
    private Integer totalCommits;
    private Integer totalLinesAdded;
    private Integer totalLinesDeleted;
    private Integer totalLinesChanged;
    private LocalDateTime firstCommitDate;
    private LocalDateTime lastCommitDate;

    public DeveloperStatsDTO() {}

    public DeveloperStatsDTO(String developerName, String developerEmail, Integer totalCommits, 
                             Integer totalLinesAdded, Integer totalLinesDeleted, Integer totalLinesChanged,
                             LocalDateTime firstCommitDate, LocalDateTime lastCommitDate) {
        this.developerName = developerName;
        this.developerEmail = developerEmail;
        this.totalCommits = totalCommits;
        this.totalLinesAdded = totalLinesAdded;
        this.totalLinesDeleted = totalLinesDeleted;
        this.totalLinesChanged = totalLinesChanged;
        this.firstCommitDate = firstCommitDate;
        this.lastCommitDate = lastCommitDate;
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
}