package com.example.demo.service;

import com.example.demo.model.Match;
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
    public List<Match> getScheduledMatches(Long ladderId) {
        return matchDao.getScheduledMatches(ladderId);
    }

    @Override       
    public Long addMatch(Match match) {
        return matchDao.addMatch(match);
    }

    @Override
    public void updateMatch(Match match) {
        matchDao.updateMatch(match);
    }
}
