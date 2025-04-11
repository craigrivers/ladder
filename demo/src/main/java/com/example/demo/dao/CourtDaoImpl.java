package com.example.demo.dao;

import com.example.demo.domain.Court;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public class CourtDaoImpl implements CourtDao {
    private final JdbcTemplate jdbcTemplate;

    private static final String GET_ALL_COURTS = """
            SELECT court_id, name, address, link, phone
            FROM court
            ORDER BY name
            """;

    CourtDaoImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<Court> getCourts() {
        return jdbcTemplate.query(GET_ALL_COURTS, (rs, rowNum) -> {
            Court court = new Court();
            court.setCourtId(rs.getInt("court_id"));
            court.setName(rs.getString("name"));
            court.setAddress(rs.getString("address"));
            court.setLink(rs.getString("link"));
            court.setPhone(rs.getString("phone"));
            return court;
        });
    }
}
