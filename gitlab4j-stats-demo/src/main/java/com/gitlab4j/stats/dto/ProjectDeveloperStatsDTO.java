package com.gitlab4j.stats.dto;

public class ProjectDeveloperStatsDTO {
    private String projectName;
    private String developerName;
    private String developerEmail;
    private int commitsCount;
    private int linesAdded;
    private int linesDeleted;
    private int linesChanged;
    private double commitFrequency;
    private String firstCommitDate;
    private String lastCommitDate;
    
    public ProjectDeveloperStatsDTO() {}
    
    public ProjectDeveloperStatsDTO(String projectName, String developerName, String developerEmail,
                                   int commitsCount, int linesAdded, int linesDeleted, int linesChanged,
                                   double commitFrequency, String firstCommitDate, String lastCommitDate) {
        this.projectName = projectName;
        this.developerName = developerName;
        this.developerEmail = developerEmail;
        this.commitsCount = commitsCount;
        this.linesAdded = linesAdded;
        this.linesDeleted = linesDeleted;
        this.linesChanged = linesChanged;
        this.commitFrequency = commitFrequency;
        this.firstCommitDate = firstCommitDate;
        this.lastCommitDate = lastCommitDate;
    }
    
    // Getters and setters
    public String getProjectName() {
        return projectName;
    }
    
    public void setProjectName(String projectName) {
        this.projectName = projectName;
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
    
    public int getCommitsCount() {
        return commitsCount;
    }
    
    public void setCommitsCount(int commitsCount) {
        this.commitsCount = commitsCount;
    }
    
    public int getLinesAdded() {
        return linesAdded;
    }
    
    public void setLinesAdded(int linesAdded) {
        this.linesAdded = linesAdded;
    }
    
    public int getLinesDeleted() {
        return linesDeleted;
    }
    
    public void setLinesDeleted(int linesDeleted) {
        this.linesDeleted = linesDeleted;
    }
    
    public int getLinesChanged() {
        return linesChanged;
    }
    
    public void setLinesChanged(int linesChanged) {
        this.linesChanged = linesChanged;
    }
    
    public double getCommitFrequency() {
        return commitFrequency;
    }
    
    public void setCommitFrequency(double commitFrequency) {
        this.commitFrequency = commitFrequency;
    }
    
    public String getFirstCommitDate() {
        return firstCommitDate;
    }
    
    public void setFirstCommitDate(String firstCommitDate) {
        this.firstCommitDate = firstCommitDate;
    }
    
    public String getLastCommitDate() {
        return lastCommitDate;
    }
    
    public void setLastCommitDate(String lastCommitDate) {
        this.lastCommitDate = lastCommitDate;
    }
}