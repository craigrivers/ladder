package com.example.demo.dao;

import java.util.List;
import com.example.demo.model.Match;

public interface MatchDao {

    List<Match> getScheduledMatches(Long ladderId);
    Long addMatch(Match match);
    void updateMatch(Match match);  

}
