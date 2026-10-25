
package com.logistics.fleet.repository;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@Repository
public class DashboardRepository {

    private final JdbcTemplate jdbcTemplate;

    public DashboardRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public long count(String sql) {
        Long result = jdbcTemplate.queryForObject(sql, Long.class);
        return result == null ? 0L : result;
    }

    public BigDecimal amount(String sql) {
        BigDecimal result =
                jdbcTemplate.queryForObject(sql, BigDecimal.class);
        return result == null ? BigDecimal.ZERO : result;
    }

    public List<Map<String, Object>> getShipmentStatuses() {
        return jdbcTemplate.queryForList(
                "SELECT STATUS, COUNT(*) AS TOTAL " +
                "FROM SHIPMENTS GROUP BY STATUS ORDER BY STATUS");
    }
}
