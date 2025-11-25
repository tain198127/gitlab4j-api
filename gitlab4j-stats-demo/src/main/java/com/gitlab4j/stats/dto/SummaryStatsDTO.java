package com.gitlab4j.stats.dto;

public class SummaryStatsDTO {
    private Integer totalDevelopers;
    private Integer totalCommits;
    private Integer totalLinesAdded;
    private Integer totalLinesDeleted;
    private Integer totalLinesChanged;
    private Integer totalProjects;

    public SummaryStatsDTO() {}

    public SummaryStatsDTO(Integer totalDevelopers, Integer totalCommits, Integer totalLinesAdded, 
                          Integer totalLinesDeleted, Integer totalLinesChanged, Integer totalProjects) {
        this.totalDevelopers = totalDevelopers;
        this.totalCommits = totalCommits;
        this.totalLinesAdded = totalLinesAdded;
        this.totalLinesDeleted = totalLinesDeleted;
        this.totalLinesChanged = totalLinesChanged;
        this.totalProjects = totalProjects;
    }

    public Integer getTotalDevelopers() {
        return totalDevelopers;
    }

    public void setTotalDevelopers(Integer totalDevelopers) {
        this.totalDevelopers = totalDevelopers;
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

    public Integer getTotalProjects() {
        return totalProjects;
    }

    public void setTotalProjects(Integer totalProjects) {
        this.totalProjects = totalProjects;
    }
}