package com.example.gitlab4jstatsdemo.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;

@Entity
@Data
public class DeveloperStats {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    private String username;
    private String name;
    private String email;
    
    private long totalAdditions;
    private long totalDeletions;
    private long totalCommits;
    
    private long firstCommitTimestamp;
    private long lastCommitTimestamp;
}