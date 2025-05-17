package com.example.demo.dao;

import com.example.demo.model.SetScore;
import java.util.List;

public interface SetScoreDao {
    public void save(SetScore setScore);
    public List<SetScore> findAll();  
    public List<SetScore> findByMatchResultId(Long matchResultId);
}
