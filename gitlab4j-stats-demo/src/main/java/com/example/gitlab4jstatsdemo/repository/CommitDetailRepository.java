package com.example.gitlab4jstatsdemo.repository;

import com.example.gitlab4jstatsdemo.entity.CommitDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface CommitDetailRepository extends JpaRepository<CommitDetail, Long> {
    Optional<CommitDetail> findByCommitId(String commitId);
}