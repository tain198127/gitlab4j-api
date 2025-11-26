package com.gitlab4j.stats.dto;

import java.util.List;
import java.util.Map;

public class MultiProjectStatsDTO {
    private int totalProjects;
    private int totalDevelopers;
    private int totalCommits;
    private int totalLinesAdded;
    private int totalLinesDeleted;
    private int totalLinesChanged;
    private Map<String, Integer> projectDeveloperDistribution;
    private List<ProjectDeveloperStatsDTO> projectDeveloperStats;
    
    public MultiProjectStatsDTO() {}
    
    public MultiProjectStatsDTO(int totalProjects, int totalDevelopers, int totalCommits, 
                               int totalLinesAdded, int totalLinesDeleted, int totalLinesChanged,
                               Map<String, Integer> projectDeveloperDistribution,
                               List<ProjectDeveloperStatsDTO> projectDeveloperStats) {
        this.totalProjects = totalProjects;
        this.totalDevelopers = totalDevelopers;
        this.totalCommits = totalCommits;
        this.totalLinesAdded = totalLinesAdded;
        this.totalLinesDeleted = totalLinesDeleted;
        this.totalLinesChanged = totalLinesChanged;
        this.projectDeveloperDistribution = projectDeveloperDistribution;
        this.projectDeveloperStats = projectDeveloperStats;
    }
    
    // Getters and setters
    public int getTotalProjects() {
        return totalProjects;
    }
    
    public void setTotalProjects(int totalProjects) {
        this.totalProjects = totalProjects;
    }
    
    public int getTotalDevelopers() {
        return totalDevelopers;
    }
    
    public void setTotalDevelopers(int totalDevelopers) {
        this.totalDevelopers = totalDevelopers;
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
    
    public Map<String, Integer> getProjectDeveloperDistribution() {
        return projectDeveloperDistribution;
    }
    
    public void setProjectDeveloperDistribution(Map<String, Integer> projectDeveloperDistribution) {
        this.projectDeveloperDistribution = projectDeveloperDistribution;
    }
    
    public List<ProjectDeveloperStatsDTO> getProjectDeveloperStats() {
        return projectDeveloperStats;
    }
    
    public void setProjectDeveloperStats(List<ProjectDeveloperStatsDTO> projectDeveloperStats) {
        this.projectDeveloperStats = projectDeveloperStats;
    }
}