package com.ig.eval.configuration;

import com.ig.eval.dao.CoffeeHouseDAO;
import com.ig.eval.service.InputValidatorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

@Configuration
public class CoffeeHouseConfiguration {
    @Autowired
    private Environment environment;

    @Bean
    public InputValidatorService inputValidatorService() {
        return new InputValidatorService();
    }


    @Bean
    public CoffeeHouseDAO coffeeHouseDAO() {
        return new CoffeeHouseDAO();
    }
}
