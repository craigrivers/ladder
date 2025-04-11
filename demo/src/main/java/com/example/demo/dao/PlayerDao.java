package com.example.demo.dao;
import com.example.demo.domain.Player;
import com.example.demo.domain.Standing;
import java.util.List;

public interface PlayerDao {
    Integer save(Player player);
    void addPlayerToLadder(Integer playerId, Integer ladderId);
    List<Standing> getStandingsByLadderId(Integer ladderId);
}
