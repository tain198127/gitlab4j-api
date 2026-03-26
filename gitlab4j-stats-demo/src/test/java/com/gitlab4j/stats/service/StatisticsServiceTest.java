package com.gitlab4j.stats.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.gitlab4j.stats.dto.DeveloperStatsDTO;
import com.gitlab4j.stats.dto.SummaryStatsDTO;
import com.gitlab4j.stats.entity.DeveloperStats;
import com.gitlab4j.stats.repository.DeveloperStatsRepository;

@ExtendWith(MockitoExtension.class)
class StatisticsServiceTest {

    @Mock
    private DeveloperStatsRepository developerStatsRepository;

    @Mock
    private DailyStatsRepository dailyStatsRepository;

    @Mock
    private ProjectStatsRepository projectStatsRepository;

    @Mock
    private CommitDetailRepository commitDetailRepository;

    @InjectMocks
    private StatisticsService statisticsService;

    private DeveloperStats testDeveloperStats;

    @BeforeEach
    void setUp() {
        testDeveloperStats = new DeveloperStats();
        testDeveloperStats.setId(1L);
        testDeveloperStats.setDeveloperName("John Doe");
        testDeveloperStats.setDeveloperEmail("john@example.com");
        testDeveloperStats.setTotalCommits(10);
        testDeveloperStats.setTotalLinesAdded(500);
        testDeveloperStats.setTotalLinesDeleted(200);
        testDeveloperStats.setTotalLinesChanged(700);
        testDeveloperStats.setFirstCommitDate(LocalDateTime.now().minusDays(30));
        testDeveloperStats.setLastCommitDate(LocalDateTime.now());
    }

    @Test
    void getDeveloperStats_ShouldReturnDeveloperStatsDTOs() {
        // Given
        List<DeveloperStats> developerStatsList = Arrays.asList(testDeveloperStats);
        when(developerStatsRepository.findTop10ByOrderByTotalLinesChangedDesc()).thenReturn(developerStatsList);

        // When
        List<DeveloperStatsDTO> result = statisticsService.getDeveloperStats(10, "linesChanged");

        // Then
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("John Doe", result.get(0).getDeveloperName());
        assertEquals("john@example.com", result.get(0).getDeveloperEmail());
        assertEquals(10, result.get(0).getTotalCommits());
        assertEquals(700, result.get(0).getTotalLinesChanged());

        verify(developerStatsRepository).findTop10ByOrderByTotalLinesChangedDesc();
    }

    @Test
    void getDeveloperStatsByEmail_ShouldReturnDeveloperStatsDTO() {
        // Given
        when(developerStatsRepository.findByDeveloperEmail(anyString())).thenReturn(testDeveloperStats);

        // When
        DeveloperStatsDTO result = statisticsService.getDeveloperStatsByEmail("john@example.com");

        // Then
        assertNotNull(result);
        assertEquals("John Doe", result.getDeveloperName());
        assertEquals("john@example.com", result.getDeveloperEmail());
        assertEquals(10, result.getTotalCommits());
        assertEquals(700, result.getTotalLinesChanged());

        verify(developerStatsRepository).findByDeveloperEmail("john@example.com");
    }

    @Test
    void getSummaryStats_ShouldReturnSummaryStatsDTO() {
        // Given
        when(developerStatsRepository.count()).thenReturn(5L);
        when(developerStatsRepository.sumTotalCommits()).thenReturn(100);
        when(developerStatsRepository.sumTotalLinesAdded()).thenReturn(5000);
        when(developerStatsRepository.sumTotalLinesDeleted()).thenReturn(2000);
        when(developerStatsRepository.sumTotalLinesChanged()).thenReturn(7000);
        when(projectStatsRepository.count()).thenReturn(3L);

        // When
        SummaryStatsDTO result = statisticsService.getSummaryStats();

        // Then
        assertNotNull(result);
        assertEquals(5, result.getTotalDevelopers());
        assertEquals(100, result.getTotalCommits());
        assertEquals(5000, result.getTotalLinesAdded());
        assertEquals(2000, result.getTotalLinesDeleted());
        assertEquals(7000, result.getTotalLinesChanged());
        assertEquals(3, result.getTotalProjects());

        verify(developerStatsRepository).count();
        verify(developerStatsRepository).sumTotalCommits();
        verify(developerStatsRepository).sumTotalLinesAdded();
        verify(developerStatsRepository).sumTotalLinesDeleted();
        verify(developerStatsRepository).sumTotalLinesChanged();
        verify(projectStatsRepository).count();
    }
}
