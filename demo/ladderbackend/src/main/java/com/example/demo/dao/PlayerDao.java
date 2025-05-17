package com.example.demo.dao;
import com.example.demo.model.Player;
import com.example.demo.model.Standing;
import java.util.List;  

public interface PlayerDao {
    Integer save(Player player);
    void addPlayerToLadder(Integer playerId, Integer ladderId);
    void updatePlayer(Integer playerId, String availability);
    List<Player> getPlayersByLadderId(Integer ladderId);
    Player login(String email, String password);
}
