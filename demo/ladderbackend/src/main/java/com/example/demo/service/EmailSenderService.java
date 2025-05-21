package com.example.demo.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.example.demo.model.Match;
import com.example.demo.model.Player;
import com.example.demo.model.MatchResult;
import com.example.demo.model.SetScore;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import com.example.demo.dao.PlayerDaoImpl;
import org.springframework.beans.factory.annotation.Autowired;
import java.util.List;

@Service
@Transactional
public class EmailSenderService {
    private static final Logger log = LoggerFactory.getLogger(EmailSenderService.class);

    private final JavaMailSender mailSender;
    private final String fromEmail;
    private final PlayerDaoImpl playerDao;

    public EmailSenderService(
            JavaMailSender mailSender,
            @Value("${spring.mail.username}") String fromEmail,
            PlayerDaoImpl playerDao) {
        this.mailSender = mailSender;
        this.fromEmail = fromEmail;
        this.playerDao = playerDao;
    }

    public void sendNotification(Match match, String notificationType, Player loggedInPlayer) {
        log.info("sendNotification:" + " loggedInPlayer: " + loggedInPlayer.toString() + " \n match: "
                + match.toString() + " \n   notificationType: " + notificationType);
        String[] playersEmails = getPlayersEmails(match, loggedInPlayer);
        log.info("playersEmails: {}", String.join(", ", playersEmails));
        String message = buildMsgBody(match, loggedInPlayer, notificationType);
        sendEmail(playersEmails, getSubject(notificationType), message);
    }

    public void sendMatchResult(MatchResult matchResult) {
        log.info("sendMatchResult:" + " matchResult: " + matchResult.toString());
        String[] playersEmails = getAllPlayersEmails();
        log.info("playersEmails: {}", String.join(", ", playersEmails));
        String message = buildMsgBody(matchResult, "match-result");
        log.info("message: {}", message);
        sendEmail(playersEmails, "Match Result", message);
    }

    private void sendEmail(String[] recipients, String subject, String message) {
        try {
            SimpleMailMessage mailMessage = new SimpleMailMessage();
            mailMessage.setFrom(fromEmail);
            mailMessage.setTo(recipients);
            mailMessage.setSubject(subject);
            mailMessage.setText(message);

            mailSender.send(mailMessage);
            log.info("Email sent successfully to multiple recipients: {}", String.join(", ", recipients));
        } catch (Exception e) {
            log.error("Failed to send email to multiple recipients. Error: {}", e.getMessage(), e);
            throw new RuntimeException("Failed to send email to multiple recipients", e);
        }
    }

    private String getSubject(String notificationType) {
        return notificationType.equals("add-match") ? "Match Scheduled" : "Match Updated";
    }

    private String[] getPlayersEmails(Match match, Player loggedInPlayer) {
        // The logged in player is the one who updated the match. Find the email address
        // of the other player.
        Long otherPlayerId = match.getPlayer1Id() == loggedInPlayer.getPlayerId() ? match.getPlayer2Id()
                : match.getPlayer1Id();
        Player otherPlayer = playerDao.getPlayerById(otherPlayerId);
        return new String[] { otherPlayer.getEmail(), loggedInPlayer.getEmail() };
    }

    private String buildMsgBody(Match match, Player loggedInPlayer, String notificationType) {
        StringBuilder message = new StringBuilder();
        message.append(notificationType.equals("add-match") ? "Match scheduled by: " : "Match updated by: ");
        message.append(loggedInPlayer.getFirstName() + " " + loggedInPlayer.getLastName() + " cell: "
                + loggedInPlayer.getCell() + "\n Modify date:" + now() +
                "\n Match details:  " + match.getMatchDetails());
        message.append("\n\n" + getOpponentName(match, loggedInPlayer)
                + " log into the app (https://dmvtennisladders.com), click on Schedule Matches, go to the My Scheduled Match Section find this match and either Confirm, Update or Decline the match.");
        return message.toString();
    }

    private String getOpponentName(Match match, Player loggedInPlayer) {
        return match.getPlayer1Id() == loggedInPlayer.getPlayerId() ? match.getPlayer2Name() : match.getPlayer1Name();
    }

    private String now() {
        return LocalDateTime.now().format(DateTimeFormatter.ofPattern("MM-dd-yy HH:mm"));
    }

    private String buildMsgBody(MatchResult matchResult, String notificationType) {
        // Get the players from the match result
        Player winner = matchResult.getMatchWinnerId().equals(matchResult.getPlayer1Id())
                ? playerDao.getPlayerById(matchResult.getPlayer1Id())
                : playerDao.getPlayerById(matchResult.getPlayer2Id());
        Player loser = matchResult.getMatchWinnerId().equals(matchResult.getPlayer1Id())
                ? playerDao.getPlayerById(matchResult.getPlayer2Id())
                : playerDao.getPlayerById(matchResult.getPlayer1Id());
        StringBuilder message = new StringBuilder();
        message.append("Opponents: " + winner.getFirstName() + " " + winner.getLastName() + " vs " );
        message.append(loser.getFirstName() + " " + loser.getLastName() + "\n");
        message.append("Winner: " + winner.getFirstName() + " " + winner.getLastName() + "\n");
        message.append("Scores: " + getFormattedScores(matchResult, winner) + "\n");
        return message.toString();
    }

    private String getFormattedScores(MatchResult matchResult, Player winner) {
        StringBuilder message = new StringBuilder();
        Integer loserSetScore = null;
        Integer winnerSetScore = null;
        for (SetScore setScore : matchResult.getSetScores()) {
            if (setScore.getSetNumber().equals(1)) {
                if (setScore.getPlayerId().equals(winner.getPlayerId())) {
                    winnerSetScore = setScore.getSetScore();
                } else {
                    loserSetScore = setScore.getSetScore();
                }
                if (loserSetScore != null && winnerSetScore != null) {
                    message.append(winnerSetScore.toString() + "-" + loserSetScore.toString() + ",");
                    loserSetScore = null;
                    winnerSetScore = null;
                }
            } else if (setScore.getSetNumber().equals(2)) {
                if (setScore.getPlayerId().equals(winner.getPlayerId())) {
                    winnerSetScore = setScore.getSetScore();
                } else {
                    loserSetScore = setScore.getSetScore();
                }
                if (loserSetScore != null && winnerSetScore != null) {
                    message.append(winnerSetScore.toString() + "-" + loserSetScore.toString() );
                    loserSetScore = null;
                    winnerSetScore = null;
                }
            } else if (setScore.getSetNumber().equals(3)) {
                if (setScore.getPlayerId().equals(winner.getPlayerId())) {
                    winnerSetScore = setScore.getSetScore();
                } else {
                    loserSetScore = setScore.getSetScore();
                }
                if (loserSetScore != null && winnerSetScore != null) {
                    // Do not display 0-0 for the third set
                    if (!(winnerSetScore.intValue() == 0 && loserSetScore.intValue() == 0)) {
                        message.append(", " + winnerSetScore.toString() + "-" + loserSetScore.toString());
                    }
                    loserSetScore = null;
                    winnerSetScore = null;
                }
            }
        }
        return message.toString();
    }

    private String[] getAllPlayersEmails() {
        List<Player> players = playerDao.getPlayersByLadderId(1L);
        return players.stream()
                .map(Player::getEmail)
                .toArray(String[]::new);
    }

    /* 
    private String getFormattedScoresDoesNotWork(MatchResult matchResult, Player winner) {
        StringBuilder message = new StringBuilder();
        Integer loserSetScore = null; // Integer is a immutable object therefore it is not updated in the method   
        Integer winnerSetScore = null;
        for (SetScore setScore : matchResult.getSetScores()) {
            if (setScore.getSetNumber().equals(1)) {
                addSetScoreToMessage(message, setScore, winner, winnerSetScore, loserSetScore); 
                log.info("getFormattedScores: " + loserSetScore + " " + winnerSetScore);
            } else if (setScore.getSetNumber().equals(2)) {
                addSetScoreToMessage(message, setScore, winner, winnerSetScore, loserSetScore);
                log.info("getFormattedScores: " + loserSetScore + " " + winnerSetScore);
            } else if (setScore.getSetNumber().equals(3)) {
                addSetScoreToMessage(message, setScore, winner, winnerSetScore, loserSetScore);
                log.info("getFormattedScores: " + loserSetScore + " " + winnerSetScore);
            }
        }
        return message.toString();
    }
/* 
    private void addSetScoreToMessage(StringBuilder message, SetScore setScore, Player winner, Integer winnerSetScore,
            Integer loserSetScore) {
        if (setScore.getPlayerId().equals(winner.getPlayerId())) {
            winnerSetScore = setScore.getSetScore();
        } else {
            loserSetScore = setScore.getSetScore();
        }
        log.info("addSetScoreToMessage: " + loserSetScore + " " + winnerSetScore);
        if (loserSetScore != null && winnerSetScore != null) {
            message.append(winnerSetScore.toString() + "-" + loserSetScore.toString() + ",");
            loserSetScore = null;
            winnerSetScore = null;
        }
    }
*/

}
