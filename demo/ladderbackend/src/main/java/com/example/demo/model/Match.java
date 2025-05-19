package com.example.demo.model;

import java.sql.Timestamp;

public class Match {
    private Long matchId;
    private Long ladderId;
    private String matchType;
    private Long player1Id;
    private Long player2Id;
    private Long courtId;
    private Timestamp matchDate;
    private String player1Name;
    private String player2Name;
    private Long player3Id;
    private String player3Name;
    private Long player4Id;
    private String player4Name;
    private String matchScheduledStatus;
    private String courtName;

    // Default constructor
    public Match() {
    }

    // Constructor for query results
    public Match(long matchId, long ladderId, String matchType, long player1Id, String player1Name,
                long player2Id, String player2Name, long player3Id, String player3Name,
                long player4Id, String player4Name, String matchDate, String matchScheduledStatus,
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

    public Long getMatchId() {
        return matchId;
    }

    public void setMatchId(Long matchId) {
        this.matchId = matchId;
    }

    public Long getLadderId() {
        return ladderId;
    }

    public void setLadderId(Long ladderId) {
        this.ladderId = ladderId;
    }

    public String getMatchType() {
        return matchType;
    }

    public void setMatchType(String matchType) {
        this.matchType = matchType;
    }

    public Long getPlayer1Id() {
        return player1Id;
    }

    public void setPlayer1Id(Long player1Id) {
        this.player1Id = player1Id;
    }

    public Long getPlayer2Id() {
        return player2Id;
    }

    public void setPlayer2Id(Long player2Id) {
        this.player2Id = player2Id;
    }

    public Long getCourtId() {
        return courtId;
    }

    public void setCourtId(Long courtId) {
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

    public Long getPlayer3Id() {
        return player3Id;
    }

    public void setPlayer3Id(Long player3Id) {
        this.player3Id = player3Id;
    }

    public String getPlayer3Name() {
        return player3Name;
    }

    public void setPlayer3Name(String player3Name) {
        this.player3Name = player3Name;
    }

    public Long getPlayer4Id() {
        return player4Id;
    }

    public void setPlayer4Id(Long player4Id) {
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

    @Override
    public String toString() {
        return "Match{" +
                "matchId=" + matchId +
                ", ladderId=" + ladderId +
                ", matchType='" + matchType + '\'' +
                ", player1Id=" + player1Id +
                ", player2Id=" + player2Id +
                ", courtId=" + courtId +
                ", matchDate=" + matchDate +
                ", player1Name='" + player1Name + '\'' +
                ", player2Name='" + player2Name + '\'' +
                ", player3Id=" + player3Id +
                ", player3Name='" + player3Name + '\'' +
                ", player4Id=" + player4Id +
                ", player4Name='" + player4Name + '\'' +
                ", matchScheduledStatus='" + matchScheduledStatus + '\'' +
                ", courtName='" + courtName + '\'' +
                '}';
    }

    public String getMatchDetails() {
            return 
                    " \n Match Date: " + matchDate +
                    " \n Player 1: " + player1Name + 
                    " \n Player 2: " + player2Name +                   
                    " \n Court Name: " + courtName +
                    " \n Match Scheduled Status:" + matchScheduledStatus ;
    }

}
