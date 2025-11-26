package com.gitlab4j.stats.dto;

public class ProjectStatsDTO {
    private Integer projectId;
    private String projectName;
    private Integer totalCommits;
    private Integer totalLinesAdded;
    private Integer totalLinesDeleted;
    private Integer totalLinesChanged;
    private Integer developerCount;

    public ProjectStatsDTO() {}

    public ProjectStatsDTO(
            Integer projectId,
            String projectName,
            Integer totalCommits,
            Integer totalLinesAdded,
            Integer totalLinesDeleted,
            Integer totalLinesChanged,
            Integer developerCount) {
        this.projectId = projectId;
        this.projectName = projectName;
        this.totalCommits = totalCommits;
        this.totalLinesAdded = totalLinesAdded;
        this.totalLinesDeleted = totalLinesDeleted;
        this.totalLinesChanged = totalLinesChanged;
        this.developerCount = developerCount;
    }

    public Integer getProjectId() {
        return projectId;
    }

    public void setProjectId(Integer projectId) {
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

    public Integer getDeveloperCount() {
        return developerCount;
    }

    public void setDeveloperCount(Integer developerCount) {
        this.developerCount = developerCount;
    }
}
