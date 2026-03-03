package com.example.demo.dao;

import java.util.List;
import com.example.demo.model.Match;
import org.springframework.jdbc.core.JdbcTemplate;  
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;   
import java.sql.PreparedStatement;
import java.sql.Statement;

@Repository
public class MatchDaoImpl implements MatchDao {
    private final JdbcTemplate jdbcTemplate;

    public MatchDaoImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    private static final String GET_SCHEDULED_MATCHES = """
            SELECT m.*,
                   CONCAT(p1.FIRST_NAME, ' ', p1.LAST_NAME) AS PLAYER1_NAME,
                   CONCAT(p2.FIRST_NAME, ' ', p2.LAST_NAME) AS PLAYER2_NAME,
                   c.NAME AS COURT_NAME 
            FROM match m 
            JOIN player p1 ON m.player1_id = p1.player_id
            JOIN player p2 ON m.player2_id = p2.player_id
            JOIN court c ON m.court_id = c.court_id
            WHERE m.ladder_id = ?
        """;

    private static final String ADD_MATCH = """
            INSERT INTO MATCH (PLAYER1_ID, PLAYER2_ID, COURT_ID, MATCH_DATE, MATCH_SCHEDULED_STATUS)
            VALUES (?, ?, ?, ?, ?)
        """;

    private static final String ADD_MATCH_GENERATED_KEY = """
            INSERT INTO MATCH (PLAYER1_ID, PLAYER2_ID, COURT_ID, MATCH_DATE, MATCH_SCHEDULED_STATUS, LADDER_ID)
            VALUES (?, ?, ?, ?, ?, ?) RETURNING MATCH_ID 
        """;

    private static final String UPDATE_MATCH = """
            UPDATE MATCH SET MATCH_SCHEDULED_STATUS = ?, MATCH_DATE = ?, COURT_ID = ? WHERE MATCH_ID = ?
        """;

    @Override
    public List<Match> getScheduledMatches(Long ladderId) { 
        return jdbcTemplate.query(GET_SCHEDULED_MATCHES, new Object[]{ladderId}, (rs, rowNum) -> {
            return new Match(
                rs.getLong("MATCH_ID"),
                rs.getLong("LADDER_ID"),
                rs.getString("MATCH_TYPE"),
                rs.getLong("PLAYER1_ID"),
                rs.getString("PLAYER1_NAME"),
                rs.getLong("PLAYER2_ID"),
                rs.getString("PLAYER2_NAME"),
                rs.getString("MATCH_DATE"),
                rs.getString("MATCH_SCHEDULED_STATUS"),
                rs.getString("COURT_NAME")
            );
        });
    }
/* 
    public void saveMatch(Match match) {
        jdbcTemplate.update(ADD_MATCH, match.getPlayer1Id(), match.getPlayer2Id(), match.getCourtId(), match.getMatchDate(), match.getMatchScheduledStatus());
    }
*/
    @Override
    public Long addMatch(Match match) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        
        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(ADD_MATCH_GENERATED_KEY, Statement.RETURN_GENERATED_KEYS);
            ps.setLong(1, match.getPlayer1Id());
            ps.setLong(2, match.getPlayer2Id());
            ps.setLong(3, match.getCourtId());
            ps.setTimestamp(4, match.getMatchDate());
            ps.setString(5, match.getMatchScheduledStatus());
            ps.setLong(6, match.getLadderId());
            ps.setTimestamp(6, match.getMatchDate());
            ps.setString(7, match.getMatchScheduledStatus());
            ps.setLong(8, match.getLadderId());
            return ps;
        }, keyHolder);  
        
        return keyHolder.getKey().longValue();
    }

    @Override
    public void updateMatch(Match match) {
        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(UPDATE_MATCH);
            ps.setString(1, match.getMatchScheduledStatus());
            ps.setTimestamp(2, match.getMatchDate());
            ps.setLong(3, match.getCourtId());
            ps.setLong(4, match.getMatchId());
            return ps;
        });
    }

}
