package com.example.demo;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;
import java.io.IOException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.http.ResponseEntity;
import java.util.Map;
import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;
import com.example.demo.model.Player;
import com.example.demo.model.Standing;
import com.example.demo.model.Court;
import com.example.demo.service.PlayerService;
import com.example.demo.service.MatchService;
import com.example.demo.model.Match;
import com.example.demo.model.MatchResult;
import com.example.demo.dto.LoginRequest;
import com.example.demo.dto.NotificationRequest;
import com.example.demo.dto.ErrorResponse;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import com.example.demo.dto.EmailRequest;
import com.example.demo.service.EmailSenderService;
import com.example.demo.service.MatchResultService;

@SpringBootApplication
@RestController
public class LadderApplication {

    @Autowired
    private PlayerService playerService;
    
    @Autowired
    private MatchService matchService;

    @Autowired
    private EmailSenderService emailSenderService;

    @Autowired
    private MatchResultService matchResultService;

    private static final Logger log = LoggerFactory.getLogger(LadderApplication.class);

    public LadderApplication(PlayerService playerService, MatchService matchService,
     EmailSenderService emailSenderService, MatchResultService matchResultService) {
        this.playerService = playerService;
        this.matchService = matchService;
        this.emailSenderService = emailSenderService;
        this.matchResultService = matchResultService;
    }

    public static void main(String[] args) {
      SpringApplication.run(LadderApplication.class, args);
    }

    @GetMapping("/hello")
    public String hello(@RequestParam(value = "name", defaultValue = "World") String name) {
      return String.format("Hello Test %s!", name);
    }

	/**
	 * Update for any grant changes
	 * 
	 * @param grant
	 * @return Grant
	 * @throws CustomException
	 * @throws IOException
	 */
	@RequestMapping(value = { "/ladder/register" }, method = { RequestMethod.POST }, produces = { "application/json" })
	public ResponseEntity<Map<String, String>> update(@RequestBody Player player) throws IOException {
        playerService.save(player);
		Map<String, String> response = new HashMap<>();
		response.put("message", "Player registered successfully");
		return ResponseEntity.ok(response);
	}
/**
  @GetMapping("/ladder/standings")
  public List<Standing> getStandingsByLadderId(@RequestParam Long ladderId) {
    List <Standing> players = playerService.getStandingsByLadderId(ladderId);
    return players;
  }
*/

  @GetMapping("/ladder/players")
  public List<Player> getPlayersByLadderId(@RequestParam Long ladderId) {
    List <Player> players = playerService.getPlayersByLadderId(ladderId);
    return players;
  }

  @RequestMapping(value = { "/ladder/players" }, method = { RequestMethod.PUT }, produces = { "application/json" })
	public ResponseEntity<Map<String, String>> updatePlayer(@RequestBody Player player) throws IOException {
    playerService.update(player);
		Map<String, String> response = new HashMap<>();
		response.put("message", "Player updated successfully");
		return ResponseEntity.ok(response);
	}

  @GetMapping("/ladder/courts")
  public ResponseEntity<List<Court>> getCourts() {
    try {
      List<Court> courts = playerService.getCourts();
      return ResponseEntity.ok(courts);
    } catch (Exception e) {
      log.error("Error fetching courts: {}", e.getMessage(), e);
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
    }
  }

  @PostMapping("/ladder/login")
  public ResponseEntity<?> login(@Valid @RequestBody LoginRequest credentials) {
    try {
      log.info("Login request received for email: {}", credentials.getEmail());
        Player player = playerService.login(credentials.getEmail(), credentials.getPassword());
        if (player != null) {
            return ResponseEntity.ok(player);
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
            .body(new ErrorResponse("Invalid credentials"));
    } catch (Exception e) {
        log.error("Login error for email {}: {}", credentials.getEmail(), e.getMessage(), e);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
            .body(new ErrorResponse("Authentication service unavailable"));
    }
  }

  @GetMapping("/ladder/scheduledMatches")
  public List<Match> getScheduledMatches(@RequestParam Long ladderId) {
    List <Match> matches = matchService.getScheduledMatches(ladderId);
    return matches;
  }

  @PostMapping("/ladder/addMatch")
  public ResponseEntity<Map<String, String>> addMatch(@RequestBody Match match) throws IOException {
      matchService.addMatch(match);
      Map<String, String> response = new HashMap<>();
      response.put("message", "Match added successfully");
      return ResponseEntity.ok(response);
  }     
/***
  @PostMapping("/ladder/sendEmail")
  public ResponseEntity<Map<String, String>> sendEmail(@RequestBody EmailRequest emailRequest) {
    log.info("Sending email to: {}", emailRequest.getEmail());
    emailSenderService.sendEmail(emailRequest.getEmail(), emailRequest.getSubject(), emailRequest.getMessage());
    Map<String, String> response = new HashMap<>();
    response.put("message", "Email sent successfully");
    return ResponseEntity.ok(response);
  }
***/

  @PostMapping("/ladder/sendNotification")
  public ResponseEntity<Map<String, String>> sendNotification(@RequestBody NotificationRequest notificationRequest) {
    log.info("Sending notification to: {}", notificationRequest.getMatch());
    emailSenderService.sendNotification(notificationRequest.getMatch(), notificationRequest.getNotificationType(), notificationRequest.getLoginPlayer());
    Map<String, String> response = new HashMap<>();
    response.put("message", "Notification sent successfully");
    return ResponseEntity.ok(response);
  }
  @PostMapping("/ladder/updateMatch")
  public void updateMatch(@RequestBody Match match) {
    log.info("Updating match: {}", match);
    matchService.updateMatch(match);
  }

  @PostMapping("/ladder/updateMatch/sendNotification")
  public ResponseEntity<Map<String, String>> updateMatchSendNotification(@RequestBody NotificationRequest notificationRequest) {
    log.info("Updating match: {}", notificationRequest.getMatch());
    try {
      if (notificationRequest.getNotificationType().equals("add-match")) {
        matchService.addMatch(notificationRequest.getMatch());
      } else {      
        matchService.updateMatch(notificationRequest.getMatch());
      }  
      emailSenderService.sendNotification(notificationRequest.getMatch(), notificationRequest.getNotificationType(), notificationRequest.getLoginPlayer());
      Map<String, String> response = new HashMap<>();
      response.put("message", "Match updated and notification sent successfully");
      return ResponseEntity.ok(response);
    } catch (Exception e) {
      log.error("Error updating match: {}", e.getMessage(), e);
      Map<String, String> response = new HashMap<>();
      response.put("message", "Error updating match: " + e.getMessage());
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
    }
  }

  @PostMapping("/ladder/saveMatchResult")
  public ResponseEntity<Map<String, String>> saveMatchResult(@RequestBody MatchResult matchResult) {
    log.info("Saving match result: {}", matchResult);
    try {
      matchResultService.save(matchResult);
      emailSenderService.sendMatchResult(matchResult);
      Map<String, String> response = new HashMap<>();
      response.put("message", "Match result saved successfully");
      return ResponseEntity.ok(response);
    } catch (Exception e) {
      log.error("Error saving match result: {}", e.getMessage(), e);
      Map<String, String> response = new HashMap<>();
      response.put("message", "Error saving match result: " + e.getMessage());
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
    }
  }
  @GetMapping("/ladder/standings")
  public List<Standing> getStandings(@RequestParam Long ladderId) {
    List <Standing> standings = matchResultService.getStanding(ladderId);
    return standings;
  }

  @GetMapping("/ladder/matchResults")
  public List<MatchResult> getMatchResults(@RequestParam Long ladderId) {
    log.info("Getting match results for ladderId: {}", ladderId);
    List <MatchResult> matchResults = matchResultService.getMatchResults(ladderId);
    log.info("Match results: {}" + matchResults + " Scores = " + matchResults.get(0).getSetScores().get(0).getSetScore());
    return matchResults;
  }
}