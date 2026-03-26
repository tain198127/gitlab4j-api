package com.gitlab4j.stats.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.gitlab4j.stats.entity.ProjectStats;

@Repository
public interface ProjectStatsRepository extends JpaRepository<ProjectStats, Long> {

    Optional<ProjectStats> findByProjectId(Long projectId);

    @Query("SELECT SUM(p.totalLinesChanged) FROM ProjectStats p")
    Integer getTotalLinesChanged();

    @Query("SELECT SUM(p.totalCommits) FROM ProjectStats p")
    Integer getTotalCommits();

    @Query("SELECT SUM(p.totalDevelopers) FROM ProjectStats p")
    Integer getTotalDevelopers();
}
