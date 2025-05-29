package com.example.demo.service;

import com.example.demo.dao.MatchResultDao;
import com.example.demo.dao.SetScoreDao;
import com.example.demo.model.MatchResult;
import com.example.demo.model.SetScore;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class MatchResultServiceImplTest {

    @Mock
    private MatchResultDao matchResultDao;

    @Mock
    private SetScoreDao setScoreDao;

    @InjectMocks
    private MatchResultServiceImpl matchResultService;

    private MatchResult matchResult;
    private SetScore setScore1;
    private SetScore setScore2;

    @BeforeEach
    void setUp() {
        // Initialize test data
        matchResult = new MatchResult();
        matchResult.setMatchResultId(1L);

        setScore1 = new SetScore();
        setScore1.setSetNumber(1);
        setScore1.setSetWinner(0);
        setScore1.setSetScore(6);
        setScore1.setPlayerId(1L);
        setScore1.setMatchResultId(1L);

        setScore2 = new SetScore();
        setScore2.setSetNumber(2);
        setScore2.setSetWinner(1);
        setScore2.setSetScore(4);
        setScore2.setPlayerId(2L);
        setScore2.setMatchResultId(1L);

        matchResult.setSetScores(Arrays.asList(setScore1, setScore2));
    }

    @Test
    void save_ShouldSaveMatchResultAndSetScores() {
        // Arrange
        // Act
        matchResultService.save(matchResult);

        // Assert
        verify(matchResultDao, times(1)).save(matchResult);
        verify(setScoreDao, times(1)).save(setScore1);
        verify(setScoreDao, times(1)).save(setScore2);
        
        // Verify that matchResultId was set on setScores
        assertEquals(matchResult.getMatchResultId(), setScore1.getMatchResultId());
        assertEquals(matchResult.getMatchResultId(), setScore2.getMatchResultId());
    }

    @Test
    void findAll_ShouldReturnAllMatchResultsWithSetScores() {
        // Arrange
        List<MatchResult> expectedMatchResults = Arrays.asList(matchResult);
        when(matchResultDao.getMatchResults(1L)).thenReturn(expectedMatchResults);
        when(setScoreDao.findByMatchResultId(anyLong()))
            .thenReturn(Arrays.asList(setScore1, setScore2));

        // Act
        List<MatchResult> actualMatchResults = matchResultService.getMatchResults(1L);

        // Assert
        assertNotNull(actualMatchResults);
        assertEquals(1, actualMatchResults.size());
        
        MatchResult result = actualMatchResults.get(0);
        assertNotNull(result.getSetScores());
        assertEquals(2, result.getSetScores().size());
        
        verify(matchResultDao, times(1)).getMatchResults(1L);
        verify(setScoreDao, times(1)).findByMatchResultId(matchResult.getMatchResultId());
    }

    @Test
    void getMatchResults_ShouldHandleEmptyResults() {
        // Arrange
        when(matchResultDao.getMatchResults(1L)).thenReturn(List.of());

        // Act
        List<MatchResult> actualMatchResults = matchResultService.getMatchResults(1L);

        // Assert
        assertNotNull(actualMatchResults);
        assertTrue(actualMatchResults.isEmpty());
        verify(matchResultDao, times(1)).getMatchResults(1L);
        verify(setScoreDao, never()).findByMatchResultId(anyLong());
    }
} 