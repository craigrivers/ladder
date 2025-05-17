package com.example.demo.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import com.example.demo.model.Match;
import com.example.demo.model.Player;

/**
 * Data Transfer Object for notification requests.
 * Used to encapsulate notification-related data for email notifications.
 */
public class NotificationRequest {
    
    @NotNull(message = "Match details are required")
    private Match match;

    @NotBlank(message = "Notification type is required")
    private String notificationType;

    @NotNull(message = "Login player details are required")
    private Player loginPlayer;

    // Default constructor
    public NotificationRequest() {
    }

    // Constructor with all fields
    public NotificationRequest(Match match, String notificationType, Player loginPlayer) {
        this.match = match;
        this.notificationType = notificationType;
        this.loginPlayer = loginPlayer;
    }

    // Getters and Setters
    public Match getMatch() {
        return match;
    }

    public void setMatch(Match match) {
        this.match = match;
    }

    public String getNotificationType() {
        return notificationType;
    }

    public void setNotificationType(String notificationType) {
        this.notificationType = notificationType;
    }

    public Player getLoginPlayer() {
        return loginPlayer;
    }

    public void setLoginPlayer(Player loginPlayer) {
        this.loginPlayer = loginPlayer;
    }

    @Override
    public String toString() {
        return "NotificationRequest{" +
                "match=" + match +
                ", notificationType='" + notificationType + '\'' +
                ", loginPlayer=" + loginPlayer +
                '}';
    }
} 