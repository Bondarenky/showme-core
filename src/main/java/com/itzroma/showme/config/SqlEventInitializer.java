package com.itzroma.showme.config;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class SqlEventInitializer {
    private final JdbcTemplate jdbcTemplate;

    @PostConstruct
    public void init() {
        String dropEvent = "DROP EVENT IF EXISTS cleanup;";

        String createEvent = "CREATE EVENT cleanup " +
                "ON SCHEDULE EVERY 2 MINUTE " +
                "DO BEGIN " +
                "DROP TEMPORARY TABLE IF EXISTS evts; " +
                "CREATE TEMPORARY TABLE IF NOT EXISTS evts AS (SELECT id, user_id " +
                "FROM email_verification_tokens " +
                "WHERE expires_at < NOW() " +
                "AND confirmed_at IS NULL); " +
                "DELETE FROM email_verification_tokens " +
                "WHERE id IN (SELECT e.id FROM evts e); " +
                "DELETE FROM users " +
                "WHERE id IN (SELECT user_id FROM evts); " +
                "DROP TEMPORARY TABLE IF EXISTS evts; " +
                "END;";

        jdbcTemplate.execute(dropEvent);
        jdbcTemplate.execute(createEvent);
    }
}
