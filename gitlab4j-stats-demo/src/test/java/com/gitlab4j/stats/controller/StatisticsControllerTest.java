package com.gitlab4j.stats.controller;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gitlab4j.stats.dto.DeveloperStatsDTO;
import com.gitlab4j.stats.dto.SummaryStatsDTO;
import com.gitlab4j.stats.service.GitLabDataCollectionService;
import com.gitlab4j.stats.service.StatisticsService;

@WebMvcTest(StatisticsController.class)
class StatisticsControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private StatisticsService statisticsService;

    @MockBean
    private GitLabDataCollectionService dataCollectionService;

    @Autowired
    private ObjectMapper objectMapper;

    private DeveloperStatsDTO testDeveloperStatsDTO;
    private SummaryStatsDTO testSummaryStatsDTO;

    @BeforeEach
    void setUp() {
        testDeveloperStatsDTO = new DeveloperStatsDTO();
        testDeveloperStatsDTO.setDeveloperName("John Doe");
        testDeveloperStatsDTO.setDeveloperEmail("john@example.com");
        testDeveloperStatsDTO.setTotalCommits(10);
        testDeveloperStatsDTO.setTotalLinesAdded(500);
        testDeveloperStatsDTO.setTotalLinesDeleted(200);
        testDeveloperStatsDTO.setTotalLinesChanged(700);
        testDeveloperStatsDTO.setFirstCommitDate(LocalDateTime.now().minusDays(30));
        testDeveloperStatsDTO.setLastCommitDate(LocalDateTime.now());

        testSummaryStatsDTO = new SummaryStatsDTO();
        testSummaryStatsDTO.setTotalDevelopers(5);
        testSummaryStatsDTO.setTotalCommits(100);
        testSummaryStatsDTO.setTotalLinesAdded(5000);
        testSummaryStatsDTO.setTotalLinesDeleted(2000);
        testSummaryStatsDTO.setTotalLinesChanged(7000);
        testSummaryStatsDTO.setTotalProjects(3);
    }

    @Test
    void getDeveloperStats_ShouldReturnDeveloperStatsList() throws Exception {
        // Given
        List<DeveloperStatsDTO> developerStatsList = Arrays.asList(testDeveloperStatsDTO);
        when(statisticsService.getDeveloperStats(anyInt(), anyString())).thenReturn(developerStatsList);

        // When & Then
        mockMvc.perform(get("/api/stats/developers")
                        .param("limit", "10")
                        .param("sortBy", "linesChanged")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].developerName").value("John Doe"))
                .andExpect(jsonPath("$[0].developerEmail").value("john@example.com"))
                .andExpect(jsonPath("$[0].totalCommits").value(10))
                .andExpect(jsonPath("$[0].totalLinesChanged").value(700));

        verify(statisticsService).getDeveloperStats(10, "linesChanged");
    }

    @Test
    void getDeveloperStatsByEmail_ShouldReturnDeveloperStats() throws Exception {
        // Given
        when(statisticsService.getDeveloperStatsByEmail(anyString())).thenReturn(testDeveloperStatsDTO);

        // When & Then
        mockMvc.perform(get("/api/stats/developers/john@example.com").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.developerName").value("John Doe"))
                .andExpect(jsonPath("$.developerEmail").value("john@example.com"))
                .andExpect(jsonPath("$.totalCommits").value(10))
                .andExpect(jsonPath("$.totalLinesChanged").value(700));

        verify(statisticsService).getDeveloperStatsByEmail("john@example.com");
    }

    @Test
    void getSummaryStats_ShouldReturnSummaryStats() throws Exception {
        // Given
        when(statisticsService.getSummaryStats()).thenReturn(testSummaryStatsDTO);

        // When & Then
        mockMvc.perform(get("/api/stats/summary").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.totalDevelopers").value(5))
                .andExpect(jsonPath("$.totalCommits").value(100))
                .andExpect(jsonPath("$.totalLinesAdded").value(5000))
                .andExpect(jsonPath("$.totalLinesDeleted").value(2000))
                .andExpect(jsonPath("$.totalLinesChanged").value(7000))
                .andExpect(jsonPath("$.totalProjects").value(3));

        verify(statisticsService).getSummaryStats();
    }

    @Test
    void collectData_ShouldReturnSuccessMessage() throws Exception {
        // Given
        doNothing().when(dataCollectionService).collectGitLabData();

        // When & Then
        mockMvc.perform(post("/api/stats/collect").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string("Data collection completed successfully"));

        verify(dataCollectionService).collectGitLabData();
    }

    @Test
    void collectProjectData_ShouldReturnSuccessMessage() throws Exception {
        // Given
        doNothing().when(dataCollectionService).collectProjectData(anyInt());

        // When & Then
        mockMvc.perform(post("/api/stats/collect/project/1").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string("Project data collection completed successfully"));

        verify(dataCollectionService).collectProjectData(1);
    }
}
