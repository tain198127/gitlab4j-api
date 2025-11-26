package com.gitlab4j.stats.dto;

import java.time.LocalDateTime;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel(description = "Developer statistics data transfer object")
public class DeveloperStatsDTO {
    @ApiModelProperty(value = "Developer name", example = "John Doe")
    private String developerName;
    
    @ApiModelProperty(value = "Developer email address", example = "john.doe@example.com")
    private String developerEmail;
    
    @ApiModelProperty(value = "Total number of commits", example = "150")
    private Integer totalCommits;
    
    @ApiModelProperty(value = "Total lines of code added", example = "5000")
    private Integer totalLinesAdded;
    
    @ApiModelProperty(value = "Total lines of code deleted", example = "2000")
    private Integer totalLinesDeleted;
    
    @ApiModelProperty(value = "Total lines of code changed (added + deleted)", example = "7000")
    private Integer totalLinesChanged;
    
    @ApiModelProperty(value = "Date of first commit", example = "2023-01-01T10:00:00")
    private LocalDateTime firstCommitDate;
    
    @ApiModelProperty(value = "Date of last commit", example = "2023-12-31T18:30:00")
    private LocalDateTime lastCommitDate;

    public DeveloperStatsDTO() {}

    public DeveloperStatsDTO(
            String developerName,
            String developerEmail,
            Integer totalCommits,
            Integer totalLinesAdded,
            Integer totalLinesDeleted,
            Integer totalLinesChanged,
            LocalDateTime firstCommitDate,
            LocalDateTime lastCommitDate) {
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
