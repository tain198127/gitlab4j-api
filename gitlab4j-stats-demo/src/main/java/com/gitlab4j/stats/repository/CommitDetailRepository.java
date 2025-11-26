package com.gitlab4j.stats.repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.gitlab4j.stats.entity.CommitDetail;

@Repository
public interface CommitDetailRepository extends JpaRepository<CommitDetail, Long> {

    Optional<CommitDetail> findByCommitSha(String commitSha);

    List<CommitDetail> findByProjectIdOrderByCommitDateDesc(Long projectId);

    List<CommitDetail> findByAuthorEmailOrderByCommitDateDesc(String authorEmail);

    Page<CommitDetail> findByAuthorEmailOrderByCommitDateDesc(String authorEmail, Pageable pageable);

    @Query("SELECT c FROM CommitDetail c WHERE c.commitDate BETWEEN :startDate AND :endDate ORDER BY c.commitDate DESC")
    List<CommitDetail> findByCommitDateBetweenOrderByCommitDateDesc(
            @Param("startDate") LocalDateTime startDate, @Param("endDate") LocalDateTime endDate);

    @Query(
            "SELECT c FROM CommitDetail c WHERE c.authorEmail = :email AND c.commitDate BETWEEN :startDate AND :endDate ORDER BY c.commitDate DESC")
    List<CommitDetail> findByAuthorEmailAndCommitDateBetweenOrderByCommitDateDesc(
            @Param("email") String authorEmail,
            @Param("startDate") LocalDateTime startDate,
            @Param("endDate") LocalDateTime endDate);

    @Query(
            "SELECT c FROM CommitDetail c WHERE c.projectId = :projectId AND c.commitDate BETWEEN :startDate AND :endDate ORDER BY c.commitDate DESC")
    List<CommitDetail> findByProjectIdAndCommitDateBetweenOrderByCommitDateDesc(
            @Param("projectId") Long projectId,
            @Param("startDate") LocalDateTime startDate,
            @Param("endDate") LocalDateTime endDate);

    @Query("SELECT COUNT(c) FROM CommitDetail c WHERE c.authorEmail = :email")
    Long countByAuthorEmail(@Param("email") String authorEmail);

    @Query("SELECT SUM(c.linesChanged) FROM CommitDetail c WHERE c.authorEmail = :email")
    Integer getTotalLinesChangedByAuthor(@Param("email") String authorEmail);

    @Query("SELECT SUM(c.linesAdded) FROM CommitDetail c WHERE c.authorEmail = :email")
    Integer getTotalLinesAddedByAuthor(@Param("email") String authorEmail);

    @Query("SELECT SUM(c.linesDeleted) FROM CommitDetail c WHERE c.authorEmail = :email")
    Integer getTotalLinesDeletedByAuthor(@Param("email") String authorEmail);
}
