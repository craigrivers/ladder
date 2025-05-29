package com.example.demo.dao;

import com.example.demo.model.MatchResult;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MatchResultDaoImplTest {

    @Mock
    private JdbcTemplate jdbcTemplate;

    private MatchResultDaoImpl matchResultDao;

    @BeforeEach
    void setUp() {
        matchResultDao = new MatchResultDaoImpl(jdbcTemplate);
    }

    @Test
    void save_ShouldSaveMatchResultAndSetGeneratedId() {
        // Arrange
        MatchResult matchResult = new MatchResult();
        matchResult.setMatchId(1L);
        matchResult.setPlayer1Id(101L);
        matchResult.setPlayer2Id(102L);
        matchResult.setMatchWinnerId(101L);
        matchResult.setMatchDate(LocalDateTime.now());

        KeyHolder keyHolder = new GeneratedKeyHolder();
        when(jdbcTemplate.update(any(), any(KeyHolder.class))).thenAnswer(invocation -> {
            KeyHolder kh = invocation.getArgument(1);
            kh.getKeyList().add(new java.util.HashMap<String, Object>() {{
                put("MATCH_RESULT_ID", 1L );
            }});
            return 1;
        });

        // Act
        matchResultDao.save(matchResult);

        // Assert
        assertEquals(1, matchResult.getMatchResultId());
        verify(jdbcTemplate).update(any(), any(KeyHolder.class));
    }

    @Test
    void getMatchResults_ShouldReturnListOfMatchResults() throws SQLException {
        // Arrange
        LocalDateTime now = LocalDateTime.now();
        MatchResult expectedResult = new MatchResult(1L, 1L, 101L, 102L, 101L, now, 1L, "Player 1", "Player 2", "Player 1");
        
        when(jdbcTemplate.query(
            eq("SELECT * FROM MATCH_RESULT WHERE LADDER_ID = ?"),
            any(RowMapper.class),
            eq(1L)
        )).thenReturn(List.of(expectedResult));

        // Act
        List<MatchResult> results = matchResultDao.getMatchResults(1L);

        // Assert
        assertNotNull(results);
        assertEquals(1, results.size());
        
        MatchResult result = results.get(0);
        assertEquals(1, result.getMatchResultId());
        assertEquals(1, result.getMatchId());
        assertEquals(101, result.getPlayer1Id());
        assertEquals(102, result.getPlayer2Id());
        assertEquals(101, result.getMatchWinnerId());
        assertEquals(now, result.getMatchDate());
        
        verify(jdbcTemplate).query(
            eq("SELECT * FROM MATCH_RESULT WHERE LADDER_ID = ?"),
            any(RowMapper.class),
            eq(1L)
        );
    }
} 