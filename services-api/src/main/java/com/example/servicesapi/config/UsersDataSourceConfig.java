package com.example.servicesapi.config;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;

@Configuration
public class UsersDataSourceConfig {

    @Bean
    @Primary
    @ConfigurationProperties("spring.datasource.users")
    public DataSourceProperties usersDataSourceProperties() {
        return new DataSourceProperties();
    }

    @Bean
    @Primary
    public DataSource usersDataSource() {
        return usersDataSourceProperties().initializeDataSourceBuilder().build();
    }

    @Bean
    @Primary
    public JdbcTemplate usersJdbcTemplate(@Qualifier("usersDataSource") DataSource dataSource) {
        return new JdbcTemplate(dataSource);
    }
}
