package com.example.demo.service;

import com.example.demo.model.MatchResult;
import java.util.List;  

public interface MatchResultService {
    public void save(MatchResult matchResult);
    public List<MatchResult> findAll();
}

