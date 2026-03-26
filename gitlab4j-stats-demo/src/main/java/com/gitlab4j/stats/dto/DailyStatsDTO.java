package com.gitlab4j.stats.dto;

import java.time.LocalDate;

public class DailyStatsDTO {
    private LocalDate statDate;
    private String developerName;
    private String developerEmail;
    private Integer commitsCount;
    private Integer linesAdded;
    private Integer linesDeleted;
    private Integer linesChanged;

    public DailyStatsDTO() {}

    public DailyStatsDTO(
            LocalDate statDate,
            String developerName,
            String developerEmail,
            Integer commitsCount,
            Integer linesAdded,
            Integer linesDeleted,
            Integer linesChanged) {
        this.statDate = statDate;
        this.developerName = developerName;
        this.developerEmail = developerEmail;
        this.commitsCount = commitsCount;
        this.linesAdded = linesAdded;
        this.linesDeleted = linesDeleted;
        this.linesChanged = linesChanged;
    }

    public LocalDate getStatDate() {
        return statDate;
    }

    public void setStatDate(LocalDate statDate) {
        this.statDate = statDate;
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

    public Integer getCommitsCount() {
        return commitsCount;
    }

    public void setCommitsCount(Integer commitsCount) {
        this.commitsCount = commitsCount;
    }

    public Integer getLinesAdded() {
        return linesAdded;
    }

    public void setLinesAdded(Integer linesAdded) {
        this.linesAdded = linesAdded;
    }

    public Integer getLinesDeleted() {
        return linesDeleted;
    }

    public void setLinesDeleted(Integer linesDeleted) {
        this.linesDeleted = linesDeleted;
    }

    public Integer getLinesChanged() {
        return linesChanged;
    }

    public void setLinesChanged(Integer linesChanged) {
        this.linesChanged = linesChanged;
    }
}
