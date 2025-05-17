package com.example.demo.model;

public class Standing {
    private Integer ladderId;
    private Integer playerId;
    private String firstName; 
    private String lastName;
    private String goesBy;
    private Integer matchesWon;
    private Integer matchesLost;  
    private Integer gamesWon;
    private Integer gamesLost;
      
    public Integer getLadderId() {
        return ladderId;
    }

    public void setLadderId(Integer ladderId) {
        this.ladderId = ladderId;
    }

    public Integer getPlayerId() {
        return playerId;
    }

    public void setPlayerId(Integer playerId) {
        this.playerId = playerId;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getGoesBy() {
        return goesBy;
    }

    public void setGoesBy(String goesBy) {
        this.goesBy = goesBy;
    }

    public Integer getMatchesWon() {
        return matchesWon;
    }

    public void setMatchesWon(Integer matchesWon) {
        this.matchesWon = matchesWon;
    }

    public Integer getMatchesLost() {
        return matchesLost;
    }

    public void setMatchesLost(Integer matchesLost) {
        this.matchesLost = matchesLost;
    }

    public Integer getGamesWon() {
        return gamesWon;
    }

    public void setGamesWon(Integer gamesWon) {
        this.gamesWon = gamesWon;
    }

    public Integer getGamesLost() {
        return gamesLost;
    }

    public void setGamesLost(Integer gamesLost) {
        this.gamesLost = gamesLost;
    }
}
