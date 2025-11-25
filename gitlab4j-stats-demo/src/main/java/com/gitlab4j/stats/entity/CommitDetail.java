package com.gitlab4j.stats.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "commit_detail")
public class CommitDetail {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "commit_sha", nullable = false, unique = true)
    private String commitSha;

    @Column(name = "project_id", nullable = false)
    private Long projectId;

    @Column(name = "project_name", nullable = false)
    private String projectName;

    @Column(name = "author_name", nullable = false)
    private String authorName;

    @Column(name = "author_email", nullable = false)
    private String authorEmail;

    @Column(name = "commit_date", nullable = false)
    private LocalDateTime commitDate;

    @Column(name = "message", columnDefinition = "TEXT")
    private String message;

    @Column(name = "lines_added", nullable = false)
    private Integer linesAdded = 0;

    @Column(name = "lines_deleted", nullable = false)
    private Integer linesDeleted = 0;

    @Column(name = "lines_changed", nullable = false)
    private Integer linesChanged = 0;

    @Column(name = "files_changed", nullable = false)
    private Integer filesChanged = 0;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }

    public CommitDetail() {}

    public CommitDetail(String commitSha, Long projectId, String projectName, 
                       String authorName, String authorEmail, LocalDateTime commitDate) {
        this.commitSha = commitSha;
        this.projectId = projectId;
        this.projectName = projectName;
        this.authorName = authorName;
        this.authorEmail = authorEmail;
        this.commitDate = commitDate;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCommitSha() {
        return commitSha;
    }

    public void setCommitSha(String commitSha) {
        this.commitSha = commitSha;
    }

    public Long getProjectId() {
        return projectId;
    }

    public void setProjectId(Long projectId) {
        this.projectId = projectId;
    }

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public String getAuthorName() {
        return authorName;
    }

    public void setAuthorName(String authorName) {
        this.authorName = authorName;
    }

    public String getAuthorEmail() {
        return authorEmail;
    }

    public void setAuthorEmail(String authorEmail) {
        this.authorEmail = authorEmail;
    }

    public LocalDateTime getCommitDate() {
        return commitDate;
    }

    public void setCommitDate(LocalDateTime commitDate) {
        this.commitDate = commitDate;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Integer getLinesAdded() {
        return linesAdded;
    }

    public void setLinesAdded(Integer linesAdded) {
        this.linesAdded = linesAdded;
    }

    public Integer getLinesDeleted() {
        return linesDeleted;
    }

    public void setLinesDeleted(Integer linesDeleted) {
        this.linesDeleted = linesDeleted;
    }

    public Integer getLinesChanged() {
        return linesChanged;
    }

    public void setLinesChanged(Integer linesChanged) {
        this.linesChanged = linesChanged;
    }

    public Integer getFilesChanged() {
        return filesChanged;
    }

    public void setFilesChanged(Integer filesChanged) {
        this.filesChanged = filesChanged;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}