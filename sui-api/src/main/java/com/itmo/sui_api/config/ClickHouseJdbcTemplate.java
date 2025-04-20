package com.itmo.sui_api.config;

import lombok.Data;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;

@Data
public class ClickHouseJdbcTemplate extends JdbcTemplate {

    private DataSource dataSource;

    public ClickHouseJdbcTemplate(DataSource dataSource) {
        super(dataSource);
        this.dataSource = dataSource;
    }
}
