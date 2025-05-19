package com.example.demo.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.example.demo.model.Match;
import com.example.demo.model.Player;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import com.example.demo.dao.PlayerDaoImpl;
import org.springframework.beans.factory.annotation.Autowired;

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
        log.info("sendNotification:" + " loggedInPlayer: " + loggedInPlayer.toString() + " \n match: " + match.toString() + " \n   notificationType: " + notificationType);
        String[] playersEmails = getPlayersEmails(match, loggedInPlayer);
        log.info("playersEmails: {}", String.join(", ", playersEmails));
        String message = buildMsgBody(match, loggedInPlayer, notificationType);
        sendEmail(playersEmails, getSubject(notificationType), message);
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
        // The logged in player is the one who updated the match.  Find the email address of the other player.
        Long otherPlayerId = match.getPlayer1Id() == loggedInPlayer.getPlayerId() ? match.getPlayer2Id() : match.getPlayer1Id();
        Player otherPlayer = playerDao.getPlayerById(otherPlayerId);
        return new String[] {otherPlayer.getEmail(), loggedInPlayer.getEmail()};
    }

    private String buildMsgBody(Match match, Player loggedInPlayer, String notificationType) {
        StringBuilder message = new StringBuilder();
        message.append(notificationType.equals("add-match") ? "Match scheduled by: " : "Match updated by: " );
        message.append(loggedInPlayer.getFirstName() + " " + loggedInPlayer.getLastName() + " cell: " + loggedInPlayer.getCell() + "\n Modify date:"   + now() +
         "\n Match details:  " + match.getMatchDetails());
         message.append("\n\n" + getOpponentName(match, loggedInPlayer) + " log into the app (https://dmvtennisladders.com), click on Schedule Matches, go to the My Scheduled Match Section find this match and either Confirm, Update or Decline the match.");
        return message.toString();
    }

    private String getOpponentName(Match match, Player loggedInPlayer) {
        return match.getPlayer1Id() == loggedInPlayer.getPlayerId() ? match.getPlayer2Name() : match.getPlayer1Name();
    }
    private String now() {
        return LocalDateTime.now().format(DateTimeFormatter.ofPattern("MM-dd-yy HH:mm"));
    }
}

