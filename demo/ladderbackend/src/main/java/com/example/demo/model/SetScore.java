package com.example.demo.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Column;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

@Entity
public class SetScore {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "SET_SCORE_ID")
    private Long setScoreId;

    @Column(name = "MATCH_RESULT_ID", nullable = false)
    private Long matchResultId;

    @Column(name = "PLAYER_ID", nullable = false)
    private Long playerId;

    @Column(name = "SET_NUMBER", nullable = false)
    private Integer setNumber;

    @Column(name = "SET_SCORE", nullable = false)
    private Integer setScore;

    @Column(name = "SET_WINNER", nullable = false)
    private Integer setWinner;

    // Default constructor
    public SetScore() {
    }

    // Parameterized constructor
    public SetScore(Long matchResultId, Long playerId, Integer setNumber, Integer setScore, Integer setWinner) {
        this.matchResultId = matchResultId;
        this.playerId = playerId;
        this.setNumber = setNumber;
        this.setScore = setScore;
        this.setWinner = setWinner;
    }

    // Getters and Setters
    public Long getSetScoreId() {
        return setScoreId;
    }

    public void setSetScoreId(Long setScoreId) {
        this.setScoreId = setScoreId;
    }

    public Long getMatchResultId() {
        return matchResultId;
    }

    public void setMatchResultId(Long matchResultId) {
        this.matchResultId = matchResultId;
    }

    public Long getPlayerId() {
        return playerId;
    }

    public void setPlayerId(Long playerId) {
        this.playerId = playerId;
    }

    public Integer getSetNumber() {
        return setNumber;
    }

    public void setSetNumber(Integer setNumber) {
        this.setNumber = setNumber;
    }

    public Integer getSetScore() {
        return setScore;
    }

    public void setSetScore(Integer setScore) {
        this.setScore = setScore;
    }

    public Integer getSetWinner() {
        return setWinner;
    }

    public void setSetWinner(Integer setWinner) {
        this.setWinner = setWinner;
    }
}
