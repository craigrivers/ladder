package com.example.demo;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;
import java.io.IOException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.context.annotation.Bean;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.http.ResponseEntity;
import java.util.Map;
import java.util.HashMap;
import java.util.List;
import com.example.demo.domain.Player;
import com.example.demo.domain.Standing;
import com.example.demo.domain.Court;
import com.example.demo.service.PlayerService;

@SpringBootApplication
@RestController
public class DemoApplication {

  @Autowired
    private PlayerService playerService;

    DemoApplication (PlayerService playerService){
        this.playerService = playerService;
    }
    public static void main(String[] args) {
      SpringApplication.run(DemoApplication.class, args);
    }

    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/ladder/**")
                        .allowedOrigins("http://localhost:4200")
                        .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
                        .allowedHeaders("*")
                        .allowCredentials(true);
            }
        };
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
		System.out.println("IN the backend code" + player);
        playerService.save(player);
		Map<String, String> response = new HashMap<>();
		response.put("message", "Player registered successfully");
		return ResponseEntity.ok(response);
	}

  @GetMapping("/ladder/standings")
  public List<Standing> getStandingsByLadderId(@RequestParam Integer ladderId) {
    List <Standing> players = playerService.getStandingsByLadderId(ladderId);
    return players;
  }

  @GetMapping("/ladder/players")
  public List<Player> getPlayersByLadderId(@RequestParam Integer ladderId) {
    List <Player> players = playerService.getPlayersByLadderId(ladderId);
    return players;
  }

  @GetMapping("/ladder/courts")
  public List<Court> getCourts() {
    List <Court> courts = playerService.getCourts();
    return courts;
  }

}
/* 
	@GetMapping("/ladder/hello")
    public String helloTest(@RequestParam(value = "name", defaultValue = "World") String name) {
      return String.format("Hello Test %s!", name);
    }
*/


/* 
@RequestMapping(value = "/ladder/hello", method = { RequestMethod.GET }, produces = {
		"application/json" })
public String  helloTest(@RequestParam(value = "name", defaultValue = "World") String name) {
	 return String.format("Hello Test %s!", name);
}
	 */


