package com.gitlab4j.stats.repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.gitlab4j.stats.entity.DailyStats;

@Repository
public interface DailyStatsRepository extends JpaRepository<DailyStats, Long> {

    Optional<DailyStats> findByStatDateAndDeveloperEmail(LocalDate statDate, String developerEmail);

    List<DailyStats> findByStatDateOrderByLinesChangedDesc(LocalDate statDate);

    List<DailyStats> findByDeveloperEmailOrderByStatDateDesc(String developerEmail);

    @Query(
            "SELECT d FROM DailyStats d WHERE d.statDate BETWEEN :startDate AND :endDate ORDER BY d.statDate DESC, d.linesChanged DESC")
    List<DailyStats> findByStatDateBetweenOrderByStatDateDescLinesChangedDesc(
            @Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate);

    @Query(
            "SELECT d FROM DailyStats d WHERE d.developerEmail = :email AND d.statDate BETWEEN :startDate AND :endDate ORDER BY d.statDate DESC")
    List<DailyStats> findByDeveloperEmailAndStatDateBetweenOrderByStatDateDesc(
            @Param("email") String developerEmail,
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate);

    @Query("SELECT d FROM DailyStats d WHERE d.statDate = :statDate")
    List<DailyStats> findByStatDate(@Param("statDate") LocalDate statDate);

    @Query("SELECT SUM(d.linesChanged) FROM DailyStats d WHERE d.statDate = :statDate")
    Integer getTotalLinesChangedByDate(@Param("statDate") LocalDate statDate);

    @Query("SELECT SUM(d.commitsCount) FROM DailyStats d WHERE d.statDate = :statDate")
    Integer getTotalCommitsByDate(@Param("statDate") LocalDate statDate);
}
