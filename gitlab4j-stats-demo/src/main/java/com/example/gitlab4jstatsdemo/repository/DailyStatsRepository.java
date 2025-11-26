package com.example.gitlab4jstatsdemo.repository;

import com.example.gitlab4jstatsdemo.entity.DailyStats;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface DailyStatsRepository extends JpaRepository<DailyStats, Long> {
    List<DailyStats> findByUsername(String username);
    List<DailyStats> findByDate(String date);
}