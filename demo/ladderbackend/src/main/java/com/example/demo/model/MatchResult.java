package com.example.demo.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "MATCH_RESULT")
public class MatchResult {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "MATCH_RESULT_ID")
    private Long matchResultId;

    @Column(name = "MATCH_ID")
    private Long matchId;

    @Column(name = "PLAYER1_ID", nullable = false)
    private Long player1Id;

    @Column(name = "PLAYER2_ID", nullable = false)
    private Long player2Id;

    @Column(name = "MATCH_WINNER_ID", nullable = false)
    private Long matchWinnerId;

    @Column(name = "MATCH_DATE", nullable = false)
    private LocalDateTime matchDate;

    private List<SetScore> setScores;

    // Default constructor required by JPA
    public MatchResult() {
    }

    // Constructor with all fields
    public MatchResult(Long matchResultId, Long matchId, Long player1Id, Long player2Id, Long matchWinnerId, LocalDateTime matchDate) {
        this.matchResultId = matchResultId;
        this.matchId = matchId;
        this.player1Id = player1Id;
        this.player2Id = player2Id;
        this.matchWinnerId = matchWinnerId;
        this.matchDate = matchDate;
    }

    // Getters
    public Long getMatchResultId() {
        return matchResultId;
    }

    public Long getMatchId() {
        return matchId;
    }

    public Long getPlayer1Id() {
        return player1Id;
    }

    public Long getPlayer2Id() {
        return player2Id;
    }

    public Long getMatchWinnerId() {
        return matchWinnerId;
    }

    public LocalDateTime getMatchDate() {
        return matchDate;
    }

    // Setters
    public void setMatchResultId(Long matchResultId) {
        this.matchResultId = matchResultId;
    }

    public void setMatchId(Long matchId) {
        this.matchId = matchId;
    }

    public void setPlayer1Id(Long player1Id) {
        this.player1Id = player1Id;
    }

    public void setPlayer2Id(Long player2Id) {
        this.player2Id = player2Id;
    }

    public void setMatchWinnerId(Long matchWinnerId) {
        this.matchWinnerId = matchWinnerId;
    }

    public void setMatchDate(LocalDateTime matchDate) {
        this.matchDate = matchDate;
    }

    public List<SetScore> getSetScores() {
        return setScores;
    }           

    public void setSetScores(List<SetScore> setScores) {
        this.setScores = setScores;
    }
    
    @Override
    public String toString() {
        return "MatchResult{" +
                "matchResultId=" + matchResultId +
                ", matchId=" + matchId +
                ", player1Id=" + player1Id +
                ", player2Id=" + player2Id +
                ", matchWinnerId=" + matchWinnerId +
                ", matchDate=" + matchDate +
                '}';
    }
}
