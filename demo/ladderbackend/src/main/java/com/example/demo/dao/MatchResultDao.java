package com.example.demo.dao;

import java.util.List;  
import com.example.demo.model.MatchResult;

public interface MatchResultDao {
    public void save(MatchResult matchResult);

    public List<MatchResult> findAll();
}