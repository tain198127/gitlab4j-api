package com.example.gitlab4jstatsdemo.repository;

import com.example.gitlab4jstatsdemo.entity.ProjectStats;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface ProjectStatsRepository extends JpaRepository<ProjectStats, Long> {
    Optional<ProjectStats> findByProjectId(Long projectId);
}