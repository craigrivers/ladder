	package com.example.demo.dao;
	import com.example.demo.domain.Player;
	import org.springframework.jdbc.core.JdbcTemplate;
	import org.springframework.jdbc.support.GeneratedKeyHolder;
	import org.springframework.jdbc.support.KeyHolder;
	import org.springframework.stereotype.Repository;
	import java.sql.PreparedStatement;
	import java.sql.Statement;
	import java.util.List;

	@Repository
	public class PlayerDaoImpl implements PlayerDao {
		private final JdbcTemplate jdbcTemplate;

		private static final String INSERT_INTO_PLAYER = "INSERT INTO PLAYER (first_name, last_name,  password,cell, email, level, court_id, availability) " +
				"VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

		private static final String INSERT_INTO_PLAYER_LADDER = "INSERT INTO PLAYER_LADDER (LADDER_ID, PLAYER_ID) VALUES (?, ?)";

		private static final String GET_PLAYERS_BY_LADDER = """
			SELECT p.PLAYER_ID, p.FIRST_NAME, p.LAST_NAME,  p.cell, p.email, pl.LADDER_ID,
			       p.AVAILABILITY, c.NAME as COURT_NAME
			FROM PLAYER p
			JOIN PLAYER_LADDER pl ON p.PLAYER_ID = pl.PLAYER_ID
			JOIN COURT c ON p.COURT_ID = c.COURT_ID
			WHERE pl.LADDER_ID = ?	
			ORDER BY p.FIRST_NAME, p.LAST_NAME
					""";

		PlayerDaoImpl(JdbcTemplate jdbcTemplate){
			this.jdbcTemplate = jdbcTemplate;
		}
		public Integer save(Player player) {
			KeyHolder keyHolder = new GeneratedKeyHolder();
			
			jdbcTemplate.update(connection -> {
				PreparedStatement ps = connection.prepareStatement(INSERT_INTO_PLAYER, Statement.RETURN_GENERATED_KEYS);
				ps.setString(1, player.getFirstName());
				ps.setString(2, player.getLastName());
				ps.setString(3, player.getPassword());		
				ps.setString(4, player.getCell());
				ps.setString(5, player.getEmail());
				ps.setFloat(6, player.getLevel());
				ps.setInt(7, player.getCourtId());
				ps.setString(8, player.getAvailability());
				return ps;
			}, keyHolder);
			
			return keyHolder.getKey().intValue();
		}

		/**
		 * Adds a player to a specific ladder
		 * @param playerId The ID of the player to add to the ladder
		 * @param ladderId The ID of the ladder to add the player to
		 */
		public void addPlayerToLadder(Integer playerId, Integer ladderId) {
			this.jdbcTemplate.update(INSERT_INTO_PLAYER_LADDER, ladderId, playerId);
		}

		@Override
		public List<Player> getPlayersByLadderId(Integer ladderId) {
			return jdbcTemplate.query(GET_PLAYERS_BY_LADDER, (rs, rowNum) -> {
				Player player = new Player();
				player.setPlayerId(rs.getInt("PLAYER_ID"));
				player.setLadderId(rs.getInt("LADDER_ID"));
				player.setFirstName(rs.getString("FIRST_NAME"));
				player.setLastName(rs.getString("LAST_NAME"));
				player.setCell(rs.getString("CELL"));
				player.setEmail(rs.getString("EMAIL"));
				player.setAvailability(rs.getString("AVAILABILITY"));
				player.setCourtName(rs.getString("COURT_NAME"));
				return player;
			}, ladderId);
		}
	}
