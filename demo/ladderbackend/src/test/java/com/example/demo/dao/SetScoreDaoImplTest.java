package com.example.demo.dao;

import com.example.demo.model.SetScore;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;

import java.util.List;
import java.util.Map;
import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SetScoreDaoImplTest {

    @Mock
    private JdbcTemplate jdbcTemplate;

    private SetScoreDaoImpl setScoreDao;

    @BeforeEach
    void setUp() {
        setScoreDao = new SetScoreDaoImpl(jdbcTemplate);
    }

    @Test
    void save_ShouldSaveSetScoreAndSetId() {
        // Arrange
        SetScore setScore = new SetScore();
        setScore.setMatchResultId(1L);
        setScore.setPlayerId(2L);
        setScore.setSetNumber(1);
        setScore.setSetScore(6);
        setScore.setSetWinner(1);

        GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();
        Map<String, Object> keys = new HashMap<>();
        keys.put("SET_SCORE_ID", 1L);
        keyHolder.getKeyList().add(keys);

        when(jdbcTemplate.update(any(), any(KeyHolder.class))).thenAnswer(invocation -> {
            KeyHolder holder = invocation.getArgument(1);
            holder.getKeyList().add(keys);
            return 1;
        });

        // Act
        setScoreDao.save(setScore);

        // Assert
        verify(jdbcTemplate).update(any(), any(KeyHolder.class));
        assertEquals(1L, setScore.getSetScoreId());
    }

    @Test
    void findAll_ShouldReturnListOfSetScores() {
        // Arrange
        List<SetScore> expectedScores = List.of(
            createSetScore(1L, 2L, 3L, 1, 6, 1)
        );
        
        when(jdbcTemplate.query(anyString(), any(RowMapper.class))).thenReturn(expectedScores);

        // Act
        List<SetScore> result = setScoreDao.findAll();

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        
        SetScore setScore = result.get(0);
        assertEquals(1L, setScore.getSetScoreId());
        assertEquals(2L, setScore.getMatchResultId());
        assertEquals(3L, setScore.getPlayerId());
        assertEquals(1, setScore.getSetNumber());
        assertEquals(6, setScore.getSetScore());
        assertEquals(1, setScore.getSetWinner());
        
        verify(jdbcTemplate).query(anyString(), any(RowMapper.class));
    }

    private SetScore createSetScore(Long setScoreId, Long matchResultId, Long playerId, 
                                  Integer setNumber, Integer setScore, Integer setWinner) {
        SetScore score = new SetScore();
        score.setSetScoreId(setScoreId);
        score.setMatchResultId(matchResultId);
        score.setPlayerId(playerId);
        score.setSetNumber(setNumber);
        score.setSetScore(setScore);
        score.setSetWinner(setWinner);
        return score;
    }
} 