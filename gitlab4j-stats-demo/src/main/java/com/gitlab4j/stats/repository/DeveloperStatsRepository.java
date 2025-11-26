package com.gitlab4j.stats.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.gitlab4j.stats.entity.DeveloperStats;

@Repository
public interface DeveloperStatsRepository extends JpaRepository<DeveloperStats, Long> {

    Optional<DeveloperStats> findByDeveloperEmail(String developerEmail);

    List<DeveloperStats> findAllByOrderByTotalLinesChangedDesc();

    List<DeveloperStats> findAllByOrderByTotalCommitsDesc();

    @Query("SELECT SUM(d.totalLinesChanged) FROM DeveloperStats d")
    Integer getTotalLinesChanged();

    @Query("SELECT SUM(d.totalCommits) FROM DeveloperStats d")
    Integer getTotalCommits();

    @Query("SELECT d FROM DeveloperStats d WHERE d.developerName LIKE %:name%")
    List<DeveloperStats> findByDeveloperNameContaining(@Param("name") String name);

    @Query("SELECT SUM(d.totalLinesAdded) FROM DeveloperStats d")
    Integer getTotalLinesAdded();

    @Query("SELECT SUM(d.totalLinesDeleted) FROM DeveloperStats d")
    Integer getTotalLinesDeleted();
}
