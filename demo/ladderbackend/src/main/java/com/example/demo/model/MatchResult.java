package com.example.demo.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Represents a tennis match result in the ladder system.
 * This entity stores the outcome of a match between two players,
 * including the winner and associated set scores.
 */
@Entity
@Table(name = "MATCH_RESULT")
public class MatchResult {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "MATCH_RESULT_ID")
    private Long matchResultId;

    @NotNull
    @Column(name = "MATCH_ID")
    private Long matchId;

    @NotNull
    @Column(name = "PLAYER1_ID", nullable = false)
    private Long player1Id;

    @NotNull
    @Column(name = "PLAYER2_ID", nullable = false)
    private Long player2Id;

    @NotNull
    @Column(name = "MATCH_WINNER_ID", nullable = false)
    private Long matchWinnerId;

    @NotNull
    @Column(name = "MATCH_DATE", nullable = false)
    private LocalDateTime matchDate;

    @NotNull
    @Column(name = "LADDER_ID", nullable = false)
    private Long ladderId;

    private String player1Name;
    private String player2Name;
    private String winnerName;

    @OneToMany(mappedBy = "matchResultId", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<SetScore> setScores;

    // Default constructor required by JPA
    public MatchResult() {
    }

    /**
     * Creates a new match result with all required fields.
     *
     * @param matchResultId The unique identifier for the match result
     * @param matchId The identifier of the match
     * @param player1Id The ID of the first player
     * @param player2Id The ID of the second player
     * @param matchWinnerId The ID of the match winner
     * @param matchDate The date and time of the match
     * @param player1Name The full name of the first player
     * @param player2Name The full name of the second player
     * @param winnerName The full name of the match winner
     */
    public MatchResult(Long matchResultId, Long matchId, Long player1Id, Long player2Id, 
                      Long matchWinnerId, LocalDateTime matchDate, Long ladderId, String player1Name, 
                      String player2Name, String winnerName) {
        this.matchResultId = matchResultId;
        this.matchId = matchId;
        this.player1Id = player1Id;
        this.player2Id = player2Id;
        this.matchWinnerId = matchWinnerId;
        this.matchDate = matchDate;
        this.ladderId = ladderId;
        this.player1Name = player1Name;
        this.player2Name = player2Name;
        this.winnerName = winnerName;
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

    public Long getLadderId() {
        return ladderId;
    }

    public String getPlayer1Name() {
        return player1Name;
    }

    public String getPlayer2Name() {
        return player2Name;
    }

    public String getWinnerName() {
        return winnerName;
    }

    public List<SetScore> getSetScores() {
        return setScores;
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

    public void setLadderId(Long ladderId) {
        this.ladderId = ladderId;
    }

    public void setPlayer1Name(String player1Name) {
        this.player1Name = player1Name;
    }

    public void setPlayer2Name(String player2Name) {
        this.player2Name = player2Name;
    }

    public void setWinnerName(String winnerName) {
        this.winnerName = winnerName;
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
                ", ladderId=" + ladderId +
                ", player1Name='" + player1Name + '\'' +
                ", player2Name='" + player2Name + '\'' +
                ", winnerName='" + winnerName + '\'' +
                '}';
    }
}
