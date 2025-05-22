package com.example.demo.service;

import com.example.demo.model.MatchResult;
import com.example.demo.model.Standing;
import com.example.demo.model.SetScore;
import com.example.demo.dao.MatchResultDao;
import com.example.demo.dao.SetScoreDao;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class MatchResultServiceImpl implements MatchResultService {
    @Autowired
    private SetScoreDao setScoreDao;

    @Autowired
    private MatchResultDao matchResultDao;

    MatchResultServiceImpl(MatchResultDao matchResultDao, SetScoreDao setScoreDao){
        this.matchResultDao = matchResultDao;
        this.setScoreDao = setScoreDao;
    }   

    @Override
    public void save(MatchResult matchResult) {
        matchResultDao.save(matchResult);
        for (SetScore setScore : matchResult.getSetScores()) {
            setScore.setMatchResultId(matchResult.getMatchResultId());
            setScoreDao.save(setScore);
        }
    }

    @Override
    public List<MatchResult> findAll() {
        List<MatchResult> matchResults =  matchResultDao.findAll();
        for (MatchResult matchResult : matchResults) {
            matchResult.setSetScores(setScoreDao.findByMatchResultId(matchResult.getMatchResultId()));
        }
        return matchResults;
    }

    @Override
    public List<Standing> getStanding(Long ladderId) {
        return matchResultDao.getStanding(ladderId);
    }
}
