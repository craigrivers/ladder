package com.example.demo.domain;

import java.sql.Timestamp;

public class Match {
    private Integer matchId;
    private Integer ladderId;
    private String matchType;
    private Integer player1Id;
    private Integer player2Id;
    private Integer courtId;
    private Timestamp matchDate;
    private String player1Name;
    private String player2Name;
    private Integer player3Id;
    private String player3Name;
    private Integer player4Id;
    private String player4Name;
    private String matchScheduledStatus;
    private String courtName;

    // Default constructor
    public Match() {
    }

    // Constructor for query results
    public Match(int matchId, int ladderId, String matchType, int player1Id, String player1Name,
                int player2Id, String player2Name, int player3Id, String player3Name,
                int player4Id, String player4Name, String matchDate, String matchScheduledStatus,
                String courtName) {
        this.matchId = matchId;
        this.ladderId = ladderId;
        this.matchType = matchType;
        this.player1Id = player1Id;
        this.player1Name = player1Name;
        this.player2Id = player2Id;
        this.player2Name = player2Name;
        this.player3Id = player3Id;
        this.player3Name = player3Name;
        this.player4Id = player4Id;
        this.player4Name = player4Name;
        this.matchDate = Timestamp.valueOf(matchDate.replace('T', ' '));
        this.matchScheduledStatus = matchScheduledStatus;
        this.courtName = courtName;
    }

    public Integer getMatchId() {
        return matchId;
    }

    public void setMatchId(Integer matchId) {
        this.matchId = matchId;
    }

    public Integer getLadderId() {
        return ladderId;
    }

    public void setLadderId(Integer ladderId) {
        this.ladderId = ladderId;
    }

    public String getMatchType() {
        return matchType;
    }

    public void setMatchType(String matchType) {
        this.matchType = matchType;
    }

    public Integer getPlayer1Id() {
        return player1Id;
    }

    public void setPlayer1Id(Integer player1Id) {
        this.player1Id = player1Id;
    }

    public Integer getPlayer2Id() {
        return player2Id;
    }

    public void setPlayer2Id(Integer player2Id) {
        this.player2Id = player2Id;
    }

    public Integer getCourtId() {
        return courtId;
    }

    public void setCourtId(Integer courtId) {
        this.courtId = courtId;
    }

    public Timestamp getMatchDate() {
        return matchDate;
    }

    public void setMatchDate(Timestamp matchDate) {
        this.matchDate = matchDate;
    }

    public String getPlayer1Name() {
        return player1Name;
    }

    public void setPlayer1Name(String player1Name) {
        this.player1Name = player1Name;
    }

    public String getPlayer2Name() {
        return player2Name;
    }

    public void setPlayer2Name(String player2Name) {
        this.player2Name = player2Name;
    }

    public Integer getPlayer3Id() {
        return player3Id;
    }

    public void setPlayer3Id(Integer player3Id) {
        this.player3Id = player3Id;
    }

    public String getPlayer3Name() {
        return player3Name;
    }

    public void setPlayer3Name(String player3Name) {
        this.player3Name = player3Name;
    }

    public Integer getPlayer4Id() {
        return player4Id;
    }

    public void setPlayer4Id(Integer player4Id) {
        this.player4Id = player4Id;
    }

    public String getPlayer4Name() {
        return player4Name;
    }

    public void setPlayer4Name(String player4Name) {
        this.player4Name = player4Name;
    }

    public String getMatchScheduledStatus() {
        return matchScheduledStatus;
    }

    public void setMatchScheduledStatus(String matchScheduledStatus) {
        this.matchScheduledStatus = matchScheduledStatus;
    }

    public String getCourtName() {
        return courtName;
    }

    public void setCourtName(String courtName) {
        this.courtName = courtName;
    }
}
