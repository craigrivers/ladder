package com.example.demo.service;
import com.example.demo.domain.Player;
import com.example.demo.domain.Standing;
import com.example.demo.domain.Court;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.Arrays;

import com.example.demo.dao.PlayerDaoImpl;
import com.example.demo.dao.CourtDaoImpl;

@Service
@Transactional
public class PlayerService {
    @Autowired
    private PlayerDaoImpl playerDao;
    @Autowired
    private CourtDaoImpl courtDao;

    PlayerService (PlayerDaoImpl playerDao){
        this.playerDao = playerDao;
    }

    public void save(Player player){
        Integer playerId = this.playerDao.save(player);
        this.playerDao.addPlayerToLadder(playerId, player.getLadderId());
    }

    public List<Standing> getStandingsByLadderId(Integer ladderId){
        List<Standing> standings = this.playerDao.getStandingsByLadderId(ladderId); 
        return standings;
    }

    public List<Court> getCourts() {
        return courtDao.getCourts();
    }
}
