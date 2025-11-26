package com.example.gitlab4jstatsdemo.repository;

import com.example.gitlab4jstatsdemo.entity.DeveloperStats;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface DeveloperStatsRepository extends JpaRepository<DeveloperStats, Long> {
    Optional<DeveloperStats> findByUsername(String username);
}