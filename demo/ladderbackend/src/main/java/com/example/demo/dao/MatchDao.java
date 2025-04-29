package com.example.demo.dao;

import java.util.List;
import com.example.demo.domain.Match;

public interface MatchDao {

    List<Match> getScheduledMatches(Integer ladderId);
    Integer addMatch(Match match);
    void updateMatch(Match match);  

}
