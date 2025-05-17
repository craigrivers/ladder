package com.example.demo.dao;

import java.util.List;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.example.demo.model.Standing;

@Repository
public class ScoreDaoImpl implements ScoreDao {

    private final JdbcTemplate jdbcTemplate;

    private static final String GET_STANDINGS_BY_LADDER = """
				SELECT p.PLAYER_ID, p.FIRST_NAME, p.LAST_NAME, pl.LADDER_ID,
					0 as MATCHES_WON, 0 as MATCHES_LOST, 0 as GAMES_WON, 0 as GAMES_LOST
				FROM PLAYER p
				JOIN PLAYER_LADDER pl ON p.PLAYER_ID = pl.PLAYER_ID
				WHERE pl.LADDER_ID = ?
				ORDER BY p.FIRST_NAME, p.LAST_NAME
				""";

    ScoreDaoImpl(JdbcTemplate jdbcTemplate){
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<Standing> getStandingsByLadderId(Integer ladderId) {
        return jdbcTemplate.query(GET_STANDINGS_BY_LADDER, (rs, rowNum) -> {
            Standing standing = new Standing();
            standing.setPlayerId(rs.getInt("PLAYER_ID"));
            standing.setLadderId(rs.getInt("LADDER_ID"));
            standing.setFirstName(rs.getString("FIRST_NAME"));
            standing.setLastName(rs.getString("LAST_NAME"));
            standing.setMatchesWon(rs.getInt("MATCHES_WON"));
            standing.setMatchesLost(rs.getInt("MATCHES_LOST"));
            standing.setGamesWon(rs.getInt("GAMES_WON"));
            standing.setGamesLost(rs.getInt("GAMES_LOST"));
            return standing;
        }, ladderId);
    }
}
