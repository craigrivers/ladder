package com.example.demo.service;
import java.util.List;
import com.example.demo.model.Match;

public interface MatchService {

    List<Match> getScheduledMatches(Integer ladderId);
    Integer addMatch(Match match);
    void updateMatch(Match match);

}
