package com.gitlab4j.stats.dto;

import java.time.LocalDate;

public class CommitTrendDTO {
    private LocalDate date;
    private Integer commitsCount;
    private Integer linesAdded;
    private Integer linesDeleted;
    private Integer linesChanged;

    public CommitTrendDTO() {}

    public CommitTrendDTO(
            LocalDate date, Integer commitsCount, Integer linesAdded, Integer linesDeleted, Integer linesChanged) {
        this.date = date;
        this.commitsCount = commitsCount;
        this.linesAdded = linesAdded;
        this.linesDeleted = linesDeleted;
        this.linesChanged = linesChanged;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
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
