package com.example.demo.dao;

import java.util.List;  
import com.example.demo.model.MatchResult;
import com.example.demo.model.Standing;

public interface MatchResultDao {
    void save(MatchResult matchResult);
    List<MatchResult> findAll();
    List<Standing> getStanding(Long ladderId);

}