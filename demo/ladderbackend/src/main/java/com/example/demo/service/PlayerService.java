package com.example.demo.service;
import com.example.demo.model.Player;
import com.example.demo.model.Standing;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import com.example.demo.dao.PlayerDaoImpl;
import com.example.demo.dao.ScoreDaoImpl;
import com.example.demo.dao.CourtDaoImpl;
import com.example.demo.model.Court;   

@Service
@Transactional
public class PlayerService {
    @Autowired
    private PlayerDaoImpl playerDao;

    @Autowired
    private ScoreDaoImpl scoreDao;

    @Autowired
    private CourtDaoImpl courtDao;
    
    PlayerService (PlayerDaoImpl playerDao, ScoreDaoImpl scoreDao, CourtDaoImpl courtDao){
        this.playerDao = playerDao;
        this.scoreDao = scoreDao;
        this.courtDao = courtDao;
    }

    public void save(Player player){
        Long playerId = this.playerDao.save(player);
        this.playerDao.addPlayerToLadder(playerId, player.getLadderId());
    }
    public List<Player> getPlayersByLadderId(Long ladderId){           
        return this.playerDao.getPlayersByLadderId(ladderId);
    }   
    public List<Standing> getStandingsByLadderId(Long ladderId){
        return this.scoreDao.getStandingsByLadderId(ladderId);
    }
    public List<Court> getCourts(){
        return this.courtDao.getCourts();
    }   
    public void update(Player player){
        this.playerDao.updatePlayer(player.getPlayerId(), player.getAvailability());
    }   

    public Player login(String email, String password){
        System.out.println("PlayerService: Login request received for email: " + email);
        return this.playerDao.login(email, password);
    }
}
