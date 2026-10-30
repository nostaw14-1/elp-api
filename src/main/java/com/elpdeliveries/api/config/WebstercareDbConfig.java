package com.elpdeliveries.api.config;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;

@Configuration
public class WebstercareDbConfig {
    @Bean(name = "WebstercareDataSource")
    @ConfigurationProperties(prefix = "webstercare")
    public DataSource webstercareDataSource() {
        return DataSourceBuilder.create().build();
    }

    @Bean(name = "WebstercareJdbcTemplate")
    public JdbcTemplate webstercareJdbcTemplate(@Qualifier("WebstercareDataSource") DataSource dataSource) {
        return new JdbcTemplate(dataSource);
    }
}
