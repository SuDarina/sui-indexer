package com.itmo.indexing_service.config;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;

@Data
//@EqualsAndHashCode(callSuper = true)
public class ClickHouseJdbcTemplate extends JdbcTemplate {

    private DataSource dataSource;

    public ClickHouseJdbcTemplate(DataSource dataSource) {
        super(dataSource);
        this.dataSource = dataSource;
    }
}
