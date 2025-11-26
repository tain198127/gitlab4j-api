package com.gitlab4j.stats.dto;

public class DeveloperActivityDTO {
    private String developerName;
    private String developerEmail;
    private Integer commitsCount;
    private Integer linesChanged;
    private Double avgLinesPerCommit;

    public DeveloperActivityDTO() {}

    public DeveloperActivityDTO(
            String developerName,
            String developerEmail,
            Integer commitsCount,
            Integer linesChanged,
            Double avgLinesPerCommit) {
        this.developerName = developerName;
        this.developerEmail = developerEmail;
        this.commitsCount = commitsCount;
        this.linesChanged = linesChanged;
        this.avgLinesPerCommit = avgLinesPerCommit;
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

    public Integer getLinesChanged() {
        return linesChanged;
    }

    public void setLinesChanged(Integer linesChanged) {
        this.linesChanged = linesChanged;
    }

    public Double getAvgLinesPerCommit() {
        return avgLinesPerCommit;
    }

    public void setAvgLinesPerCommit(Double avgLinesPerCommit) {
        this.avgLinesPerCommit = avgLinesPerCommit;
    }
}
