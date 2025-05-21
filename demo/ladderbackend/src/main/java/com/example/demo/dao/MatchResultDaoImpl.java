package com.example.demo.dao;

import com.example.demo.model.MatchResult;
import org.springframework.stereotype.Repository;
import java.util.List;
import com.example.demo.model.Standing;
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
    private static final String SELECT_STANDING = """
                with match_winner as (
                select p.player_id, p.first_name, p.last_name, count(m.match_winner_id) as matches_won
                from match_result m
                right join player p on m.match_winner_id = p.player_id
                join player_ladder pl on pl.player_id = p.player_id
                where pl.ladder_id = ?
                group by p.player_id, p.first_name, p.last_name
            ), standings as (
               select mw.player_id, mw.first_name, mw.last_name, mw.matches_won, sum(COALESCE(s.set_winner,0)) as sets_won, sum(coalesce(s.set_score,0)) as games_won
               from match_winner mw
               left join set_score s on mw.player_id = s.player_id
               group by mw.player_id, mw.first_name, mw.last_name, mw.matches_won
            )
            select * from standings s
            order by (s.matches_won, s.sets_won, s.games_won) desc;""";

    MatchResultDaoImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public void save(MatchResult matchResult) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(INSERT_INTO_MATCH_RESULT,
                    new String[] { "match_result_id" });
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

    @Override
    public List<Standing> getStanding(Long ladderId) {
        return jdbcTemplate.query(SELECT_STANDING, (rs, rowNum) -> {
            Standing standing = new Standing();
            standing.setPlayerId(rs.getLong("PLAYER_ID"));
            standing.setFirstName(rs.getString("FIRST_NAME"));
            standing.setLastName(rs.getString("LAST_NAME"));    
            //standing.setLadderId(rs.getLong("LADDER_ID"));
            standing.setMatchesWon(rs.getInt("MATCHES_WON"));
            standing.setSetsWon(rs.getInt("SETS_WON"));
            standing.setGamesWon(rs.getInt("GAMES_WON"));
            //standing.setMatchesLost(rs.getInt("MATCHES_LOST"));
            //standing.setSetsLost(rs.getInt("SETS_LOST"));
            //standing.setGamesLost(rs.getInt("GAMES_LOST"));
            return standing;
        }, ladderId);
    }
}
