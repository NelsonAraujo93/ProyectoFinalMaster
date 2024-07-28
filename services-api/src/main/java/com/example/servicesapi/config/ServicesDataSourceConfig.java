package com.example.servicesapi.config;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;

@Configuration
public class ServicesDataSourceConfig {

    @Bean
    @ConfigurationProperties("spring.datasource.services")
    public DataSourceProperties servicesDataSourceProperties() {
        return new DataSourceProperties();
    }

    @Bean
    public DataSource servicesDataSource() {
        return servicesDataSourceProperties().initializeDataSourceBuilder().build();
    }

    @Bean
    public JdbcTemplate servicesJdbcTemplate(@Qualifier("servicesDataSource") DataSource dataSource) {
        return new JdbcTemplate(dataSource);
    }
}
