package com.example.demo.dao;

import com.example.demo.model.MatchResult;
import org.springframework.stereotype.Repository;
import java.util.List;
import com.example.demo.model.Player;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Repository
public class MatchResultDaoImpl implements MatchResultDao {

    private static final Logger logger = LoggerFactory.getLogger(MatchResultDaoImpl.class);
    private final JdbcTemplate jdbcTemplate;

    private static final String INSERT_INTO_MATCH_RESULT = "INSERT INTO MATCH_RESULT (MATCH_ID, PLAYER1_ID, PLAYER2_ID, MATCH_WINNER_ID, MATCH_DATE) VALUES (?, ?, ?, ?, ?)";

    MatchResultDaoImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public void save(MatchResult matchResult) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(INSERT_INTO_MATCH_RESULT,
                    Statement.RETURN_GENERATED_KEYS);
            ps.setLong(1, matchResult.getMatchId());
            ps.setLong(2, matchResult.getPlayer1Id());
            ps.setLong(3, matchResult.getPlayer2Id());
            ps.setLong(4, matchResult.getMatchWinnerId());
            ps.setTimestamp(5, Timestamp.valueOf(matchResult.getMatchDate()));
            return ps;
        }, keyHolder);
        matchResult.setMatchResultId(keyHolder.getKey().longValue());
    }

    @Override
    public List<MatchResult> findAll() {
        return jdbcTemplate.query("SELECT * FROM MATCH_RESULT", (rs, rowNum) -> {
            return new MatchResult(rs.getLong("MATCH_RESULT_ID"), rs.getLong("MATCH_ID"), rs.getLong("PLAYER1_ID"),
                    rs.getLong("PLAYER2_ID"), rs.getLong("MATCH_WINNER_ID"),
                    rs.getTimestamp("MATCH_DATE").toLocalDateTime());
        });
    }
}
