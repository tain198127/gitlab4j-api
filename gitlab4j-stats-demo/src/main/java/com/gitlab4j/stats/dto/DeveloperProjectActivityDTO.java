package com.gitlab4j.stats.dto;

public class DeveloperProjectActivityDTO {
    private String projectName;
    private int commitsCount;
    private int linesAdded;
    private int linesDeleted;
    private int linesChanged;
    private String firstCommitDate;
    private String lastCommitDate;
    private double activityScore;
    
    public DeveloperProjectActivityDTO() {}
    
    public DeveloperProjectActivityDTO(String projectName, int commitsCount, int linesAdded, 
                                     int linesDeleted, int linesChanged, String firstCommitDate, 
                                     String lastCommitDate, double activityScore) {
        this.projectName = projectName;
        this.commitsCount = commitsCount;
        this.linesAdded = linesAdded;
        this.linesDeleted = linesDeleted;
        this.linesChanged = linesChanged;
        this.firstCommitDate = firstCommitDate;
        this.lastCommitDate = lastCommitDate;
        this.activityScore = activityScore;
    }
    
    // Getters and setters
    public String getProjectName() {
        return projectName;
    }
    
    public void setProjectName(String projectName) {
        this.projectName = projectName;
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
    
    public double getActivityScore() {
        return activityScore;
    }
    
    public void setActivityScore(double activityScore) {
        this.activityScore = activityScore;
    }
}