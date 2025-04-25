package com.itmo.indexing_service.config;

import com.clickhouse.jdbc.ClickHouseDataSource;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;
import java.sql.SQLException;
import java.util.Properties;

@Configuration
public class ClickHouseConfig {

    @Value("${clickhouse.url}")
    private String url;

    @Value("${clickhouse.user}")
    private String user;

    @Value("${clickhouse.password}")
    private String password;

    @Bean(name = "clickhouseDataSource")
    public DataSource clickhouseDataSource() throws SQLException {
        Properties properties = new Properties();
        properties.setProperty("user", user);
        properties.setProperty("password", password);
        return new ClickHouseDataSource(url, properties);
    }

    @Bean
    public ClickHouseJdbcTemplate clickhouseJdbcTemplate(
            @Qualifier("clickhouseDataSource") DataSource dataSource) {
        return new ClickHouseJdbcTemplate(dataSource);
    }
}