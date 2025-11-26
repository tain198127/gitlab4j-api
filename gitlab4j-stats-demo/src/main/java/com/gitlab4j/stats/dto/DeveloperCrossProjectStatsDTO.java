package com.gitlab4j.stats.dto;

import java.util.List;
import java.util.Map;

public class DeveloperCrossProjectStatsDTO {
    private String developerName;
    private String developerEmail;
    private int totalProjects;
    private int totalCommits;
    private int totalLinesAdded;
    private int totalLinesDeleted;
    private int totalLinesChanged;
    private double averageCommitsPerProject;
    private double averageLinesPerProject;
    private Map<String, Integer> projectCommitCounts;
    private List<DeveloperProjectActivityDTO> projectActivities;
    private String mostActiveProject;
    private String leastActiveProject;
    private double commitFrequencyScore;
    
    public DeveloperCrossProjectStatsDTO() {}
    
    public DeveloperCrossProjectStatsDTO(String developerName, String developerEmail, int totalProjects,
                                        int totalCommits, int totalLinesAdded, int totalLinesDeleted, int totalLinesChanged,
                                        double averageCommitsPerProject, double averageLinesPerProject,
                                        Map<String, Integer> projectCommitCounts,
                                        List<DeveloperProjectActivityDTO> projectActivities,
                                        String mostActiveProject, String leastActiveProject,
                                        double commitFrequencyScore) {
        this.developerName = developerName;
        this.developerEmail = developerEmail;
        this.totalProjects = totalProjects;
        this.totalCommits = totalCommits;
        this.totalLinesAdded = totalLinesAdded;
        this.totalLinesDeleted = totalLinesDeleted;
        this.totalLinesChanged = totalLinesChanged;
        this.averageCommitsPerProject = averageCommitsPerProject;
        this.averageLinesPerProject = averageLinesPerProject;
        this.projectCommitCounts = projectCommitCounts;
        this.projectActivities = projectActivities;
        this.mostActiveProject = mostActiveProject;
        this.leastActiveProject = leastActiveProject;
        this.commitFrequencyScore = commitFrequencyScore;
    }
    
    // Getters and setters
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
    
    public int getTotalProjects() {
        return totalProjects;
    }
    
    public void setTotalProjects(int totalProjects) {
        this.totalProjects = totalProjects;
    }
    
    public int getTotalCommits() {
        return totalCommits;
    }
    
    public void setTotalCommits(int totalCommits) {
        this.totalCommits = totalCommits;
    }
    
    public int getTotalLinesAdded() {
        return totalLinesAdded;
    }
    
    public void setTotalLinesAdded(int totalLinesAdded) {
        this.totalLinesAdded = totalLinesAdded;
    }
    
    public int getTotalLinesDeleted() {
        return totalLinesDeleted;
    }
    
    public void setTotalLinesDeleted(int totalLinesDeleted) {
        this.totalLinesDeleted = totalLinesDeleted;
    }
    
    public int getTotalLinesChanged() {
        return totalLinesChanged;
    }
    
    public void setTotalLinesChanged(int totalLinesChanged) {
        this.totalLinesChanged = totalLinesChanged;
    }
    
    public double getAverageCommitsPerProject() {
        return averageCommitsPerProject;
    }
    
    public void setAverageCommitsPerProject(double averageCommitsPerProject) {
        this.averageCommitsPerProject = averageCommitsPerProject;
    }
    
    public double getAverageLinesPerProject() {
        return averageLinesPerProject;
    }
    
    public void setAverageLinesPerProject(double averageLinesPerProject) {
        this.averageLinesPerProject = averageLinesPerProject;
    }
    
    public Map<String, Integer> getProjectCommitCounts() {
        return projectCommitCounts;
    }
    
    public void setProjectCommitCounts(Map<String, Integer> projectCommitCounts) {
        this.projectCommitCounts = projectCommitCounts;
    }
    
    public List<DeveloperProjectActivityDTO> getProjectActivities() {
        return projectActivities;
    }
    
    public void setProjectActivities(List<DeveloperProjectActivityDTO> projectActivities) {
        this.projectActivities = projectActivities;
    }
    
    public String getMostActiveProject() {
        return mostActiveProject;
    }
    
    public void setMostActiveProject(String mostActiveProject) {
        this.mostActiveProject = mostActiveProject;
    }
    
    public String getLeastActiveProject() {
        return leastActiveProject;
    }
    
    public void setLeastActiveProject(String leastActiveProject) {
        this.leastActiveProject = leastActiveProject;
    }
    
    public double getCommitFrequencyScore() {
        return commitFrequencyScore;
    }
    
    public void setCommitFrequencyScore(double commitFrequencyScore) {
        this.commitFrequencyScore = commitFrequencyScore;
    }
}