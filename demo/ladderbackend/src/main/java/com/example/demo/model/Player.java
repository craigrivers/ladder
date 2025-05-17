package com.example.demo.model;

public class Player {
    String firstName;
    String lastName;
    String goesBy;
    String cell;
    String email;
    float level;
    Integer ladderId;
    Integer playerId;
    Integer courtId;
    String availability;
    String password;
    Integer matchesWon;
    Integer matchesLost;  
    Integer gamesWon;
    Integer gamesLost;
    String courtName;

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

    public String getCell() {
        return cell;
    }

    public void setCell(String cell) {
        this.cell = cell;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String emailAddress) {
        this.email = emailAddress;
    }

    public float getLevel() {
        return level;
    }

    public void setLevel(float level) {
        this.level = level;
    }

    public Integer getLadderId(){
        return ladderId;
    }

    public void setLadderId(Integer ladderId){
        this.ladderId = ladderId;
    }

    public Integer getPlayerId(){
        return playerId;
    }

    public void setPlayerId(Integer playerId){
        this.playerId = playerId;
    }

    public Integer getCourtId(){
        return courtId;
    }

    public void setCourtId(Integer courtId){
        this.courtId = courtId;
    }

    public String getAvailability(){
        return availability;
    }

    public void setAvailability(String availability){
        this.availability = availability;
    }

    public String getPassword(){
        return password;
    }

    public void setPassword(String password){
        this.password = password;
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

    public String getCourtName() {
        return courtName;
    }
    
    public void setCourtName(String courtName) {
        this.courtName = courtName;
    }

    @Override
    public String toString() {
        return "Player{" +
                "playerId=" + playerId +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", goesBy='" + goesBy + '\'' +
                ", email='" + email + '\'' +
                ", cell='" + cell + '\'' +
                ", level=" + level +
                ", ladderId=" + ladderId +
                ", courtId=" + courtId +
                ", courtName='" + courtName + '\'' +
                ", availability='" + availability + '\'' +
                ", matchesWon=" + matchesWon +
                ", matchesLost=" + matchesLost +
                ", gamesWon=" + gamesWon +
                ", gamesLost=" + gamesLost +
                '}';
    }
}
