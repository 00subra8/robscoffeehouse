package com.ig.eval.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.jdbc.core.JdbcTemplate;

@Configuration
@PropertySource("classpath:application.properties")
public class CoffeeHouseDAO {

    @Autowired
    private JdbcTemplate jdbcTemplate;



}
