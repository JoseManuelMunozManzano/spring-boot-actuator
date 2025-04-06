package com.jmunoz.footballobs.actuator;

import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;

// Indicador de health
public class FootballHealthIndicator implements HealthIndicator {
    private final JdbcTemplate template;

    public FootballHealthIndicator(JdbcTemplate template) {
        this.template = template;
    }

    @Override
    public Health health() {
        try {
            // Realmente no queremos consultar nada
            template.execute("SELECT 1");
            return Health.up().build();
        } catch (DataAccessException e) {
            return Health.down().withDetail("Cannot connect to database", e).build();
        }
    }
}
