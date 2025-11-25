package com.gitlab4j.stats.service;

import com.gitlab4j.stats.entity.CommitDetail;
import com.gitlab4j.stats.entity.DeveloperStats;
import com.gitlab4j.stats.entity.DailyStats;
import com.gitlab4j.stats.entity.ProjectStats;
import com.gitlab4j.stats.repository.CommitDetailRepository;
import com.gitlab4j.stats.repository.DeveloperStatsRepository;
import com.gitlab4j.stats.repository.DailyStatsRepository;
import com.gitlab4j.stats.repository.ProjectStatsRepository;
import org.gitlab4j.api.CommitsApi;
import org.gitlab4j.api.GitLabApi;
import org.gitlab4j.api.models.Commit;
import org.gitlab4j.api.models.Diff;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class GitLabDataCollectionServiceTest {

    @Mock
    private GitLabApi gitLabApi;

    @Mock
    private CommitsApi commitsApi;

    @Mock
    private DeveloperStatsRepository developerStatsRepository;

    @Mock
    private DailyStatsRepository dailyStatsRepository;

    @Mock
    private ProjectStatsRepository projectStatsRepository;

    @Mock
    private CommitDetailRepository commitDetailRepository;

    @InjectMocks
    private GitLabDataCollectionService dataCollectionService;

    private Commit testCommit;
    private Diff testDiff;

    @BeforeEach
    void setUp() {
        testCommit = new Commit();
        testCommit.setId("abc123");
        testCommit.setShortId("abc123");
        testCommit.setTitle("Test commit");
        testCommit.setAuthorName("John Doe");
        testCommit.setAuthorEmail("john@example.com");
        testCommit.setCreatedAt(new Date());
        testCommit.setMessage("Test commit message");

        testDiff = new Diff();
        testDiff.setDiff("@@ -1,5 +1,7 @@\n line1\n+line2\n line3\n-line4\n+line5\n line6");
        testDiff.setNewPath("src/test.java");
        testDiff.setOldPath("src/test.java");
        testDiff.setNewFile(false);
        testDiff.setRenamedFile(false);
        testDiff.setDeletedFile(false);
    }

    @Test
    void collectProjectData_ShouldProcessCommitsAndUpdateStats() throws Exception {
        // Given
        when(gitLabApi.getCommitsApi()).thenReturn(commitsApi);
        when(commitsApi.getCommits(anyInt())).thenReturn(Arrays.asList(testCommit));
        when(commitsApi.getDiff(anyInt(), anyString())).thenReturn(Arrays.asList(testDiff));
        
        DeveloperStats existingDeveloperStats = new DeveloperStats();
        existingDeveloperStats.setId(1L);
        existingDeveloperStats.setDeveloperName("John Doe");
        existingDeveloperStats.setDeveloperEmail("john@example.com");
        existingDeveloperStats.setTotalCommits(5);
        existingDeveloperStats.setTotalLinesAdded(100);
        existingDeveloperStats.setTotalLinesDeleted(50);
        existingDeveloperStats.setTotalLinesChanged(150);
        existingDeveloperStats.setFirstCommitDate(LocalDateTime.now().minusDays(10));
        existingDeveloperStats.setLastCommitDate(LocalDateTime.now().minusDays(1));

        when(developerStatsRepository.findByDeveloperEmail(anyString()))
            .thenReturn(existingDeveloperStats);
        when(projectStatsRepository.findByProjectId(anyInt())).thenReturn(null);

        // When
        dataCollectionService.collectProjectData(1);

        // Then
        verify(commitsApi).getCommits(1);
        verify(commitsApi).getDiff(1, "abc123");
        verify(developerStatsRepository).findByDeveloperEmail("john@example.com");
        verify(developerStatsRepository).save(any(DeveloperStats.class));
        verify(commitDetailRepository).save(any(CommitDetail.class));
    }

    @Test
    void parseDiffLines_ShouldCalculateCorrectly() {
        // Given
        String diffContent = "@@ -1,5 +1,7 @@\n" +
                           " line1\n" +
                           "+line2\n" +
                           " line3\n" +
                           "-line4\n" +
                           "+line5\n" +
                           " line6";

        // When
        int[] result = dataCollectionService.parseDiffLines(diffContent);

        // Then
        assertEquals(2, result[0]); // linesAdded
        assertEquals(1, result[1]); // linesDeleted
        assertEquals(3, result[2]); // linesChanged
    }

    @Test
    void updateDeveloperStats_ShouldUpdateExistingStats() {
        // Given
        DeveloperStats existingStats = new DeveloperStats();
        existingStats.setId(1L);
        existingStats.setDeveloperName("John Doe");
        existingStats.setDeveloperEmail("john@example.com");
        existingStats.setTotalCommits(5);
        existingStats.setTotalLinesAdded(100);
        existingStats.setTotalLinesDeleted(50);
        existingStats.setTotalLinesChanged(150);
        existingStats.setFirstCommitDate(LocalDateTime.now().minusDays(10));
        existingStats.setLastCommitDate(LocalDateTime.now().minusDays(1));

        Commit commit = new Commit();
        commit.setCreatedAt(new Date());

        // When
        dataCollectionService.updateDeveloperStats(existingStats, commit, 10, 5, 15);

        // Then
        assertEquals(6, existingStats.getTotalCommits());
        assertEquals(110, existingStats.getTotalLinesAdded());
        assertEquals(55, existingStats.getTotalLinesDeleted());
        assertEquals(165, existingStats.getTotalLinesChanged());
        assertNotNull(existingStats.getLastCommitDate());
    }

    @Test
    void updateDailyStats_ShouldCreateNewDailyStats() {
        // Given
        when(dailyStatsRepository.findByStatDateAndDeveloperEmail(any(), anyString()))
            .thenReturn(null);

        LocalDate commitDate = LocalDate.now();

        // When
        dataCollectionService.updateDailyStats("John Doe", "john@example.com", commitDate, 5, 10, 5, 15);

        // Then
        verify(dailyStatsRepository).save(any(DailyStats.class));
    }
}