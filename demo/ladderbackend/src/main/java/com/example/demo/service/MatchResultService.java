package com.example.demo.service;

import com.example.demo.model.MatchResult;
import com.example.demo.model.Standing;
import java.util.List;

public interface MatchResultService {
    void save(MatchResult matchResult);
    List<MatchResult> getMatchResults(Long ladderId);
    List<Standing> getStanding(Long ladderId);
}

