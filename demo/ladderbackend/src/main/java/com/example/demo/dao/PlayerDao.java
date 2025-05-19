package com.example.demo.dao;
import com.example.demo.model.Player;
import com.example.demo.model.Standing;
import java.util.List;  

public interface PlayerDao {
    Long save(Player player);
    void addPlayerToLadder(Long playerId, Long ladderId);
    void updatePlayer(Long playerId, String availability);
    List<Player> getPlayersByLadderId(Long ladderId);
    Player login(String email, String password);
}
