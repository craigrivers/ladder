package com.example.demo.dao;

import java.util.List;  
import com.example.demo.model.MatchResult;
import com.example.demo.model.Standing;

public interface MatchResultDao {
    void save(MatchResult matchResult);
    List<MatchResult> getMatchResults(Long ladderId);
    List<Standing> getStanding(Long ladderId);

}