package com.example.gitlab4jstatsdemo.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;

@Entity
@Data
public class CommitDetail {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    private String commitId;
    private String message;
    private long timestamp;
    
    private String username;
    private String name;
    private String email;
    
    private long additions;
    private long deletions;
}