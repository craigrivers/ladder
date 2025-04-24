package com.example.demo.service;

import com.example.demo.domain.Match;
import com.example.demo.dao.MatchDao;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.beans.factory.annotation.Autowired;

@Service
@Transactional

    public class MatchServiceImpl implements MatchService   {

    @Autowired
    private MatchDao matchDao;

    MatchServiceImpl(MatchDao matchDao){
        this.matchDao = matchDao;
    }

    @Override
    public List<Match> getScheduledMatches(Integer ladderId) {
        System.out.println("in getScheduledMatches service code " + ladderId);
        return matchDao.getScheduledMatches(ladderId);
    }

    @Override       
    public Integer addMatch(Match match) {
        return matchDao.addMatch(match);
    }

    @Override
    public void updateMatch(Match match) {
        matchDao.updateMatch(match);
    }
}
