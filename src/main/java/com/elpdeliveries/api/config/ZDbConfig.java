package com.elpdeliveries.api.config;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;

@Configuration
public class ZDbConfig {
    @Bean(name = "ZDataSource")
    @ConfigurationProperties(prefix = "zdb")
    public DataSource ZDataSource() {
        return DataSourceBuilder.create().build();
    }

    @Bean(name = "ZJdbcTemplate")
    public JdbcTemplate ZJdbcTemplate(@Qualifier("ZDataSource") DataSource dataSource) {
        return new JdbcTemplate(dataSource);
    }
}
