package com.itmo.sui_api.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;

@Configuration
public class ClickHouseConfig {

    @Bean
    @ConfigurationProperties("spring.clickhouse.datasource")
    public DataSource clickhouseDataSource() {
        return DataSourceBuilder.create().build();
    }

    @Bean("clickhouseJdbcTemplate")
    public ClickHouseJdbcTemplate clickhouseJdbcTemplate() {
        return new ClickHouseJdbcTemplate(clickhouseDataSource());
    }

}