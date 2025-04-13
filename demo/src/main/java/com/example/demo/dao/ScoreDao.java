package com.example.demo.dao;
import com.example.demo.domain.Standing;
import java.util.List;  

public interface ScoreDao {
    List<Standing> getStandingsByLadderId(Integer ladderId);
}
