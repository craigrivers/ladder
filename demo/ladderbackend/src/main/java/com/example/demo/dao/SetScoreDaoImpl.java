package com.example.demo.dao;

import com.example.demo.model.SetScore;
import org.springframework.stereotype.Repository;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import java.sql.PreparedStatement;
import java.sql.Statement;

@Repository
public class SetScoreDaoImpl implements SetScoreDao {

    private static final Logger logger = LoggerFactory.getLogger(SetScoreDaoImpl.class);
    private final JdbcTemplate jdbcTemplate;

    private static final String INSERT_INTO_SET_SCORE = "INSERT INTO SET_SCORE (MATCH_RESULT_ID, PLAYER_ID, SET_NUMBER, SET_SCORE, SET_WINNER) VALUES (?, ?, ?, ?, ?)";

    SetScoreDaoImpl(JdbcTemplate jdbcTemplate){
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public void save(SetScore setScore){
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(INSERT_INTO_SET_SCORE, Statement.RETURN_GENERATED_KEYS);
            ps.setLong(1, setScore.getMatchResultId());
            ps.setLong(2, setScore.getPlayerId());
            ps.setInt(3, setScore.getSetNumber());
            ps.setInt(4, setScore.getSetScore());
            ps.setInt(5, setScore.getSetWinner());
            return ps;
        }, keyHolder);
        setScore.setSetScoreId(keyHolder.getKey().longValue());
    }

    @Override
    public List<SetScore> findAll(){
        return jdbcTemplate.query("SELECT * FROM SET_SCORE", (rs, rowNum) -> {
            SetScore setScore = new SetScore();
            setScore.setSetScoreId(rs.getLong("SET_SCORE_ID"));
            setScore.setMatchResultId(rs.getLong("MATCH_RESULT_ID"));
            setScore.setPlayerId(rs.getLong("PLAYER_ID"));
            setScore.setSetNumber(rs.getInt("SET_NUMBER"));
            setScore.setSetScore(rs.getInt("SET_SCORE"));
            setScore.setSetWinner(rs.getInt("SET_WINNER"));
            return setScore;
        }); 
    }

    @Override
    public List<SetScore> findByMatchResultId(Long matchResultId){
        return jdbcTemplate.query("SELECT * FROM SET_SCORE WHERE MATCH_RESULT_ID = ?", (rs, rowNum) -> {
            SetScore setScore = new SetScore();
            setScore.setSetScoreId(rs.getLong("SET_SCORE_ID"));
            setScore.setMatchResultId(rs.getLong("MATCH_RESULT_ID"));
            setScore.setPlayerId(rs.getLong("PLAYER_ID"));
            setScore.setSetNumber(rs.getInt("SET_NUMBER"));
            setScore.setSetScore(rs.getInt("SET_SCORE"));
            setScore.setSetWinner(rs.getInt("SET_WINNER"));
            return setScore;
        }, matchResultId);
    }   
}   
    