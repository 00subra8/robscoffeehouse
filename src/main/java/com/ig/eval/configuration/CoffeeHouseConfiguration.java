package com.ig.eval.configuration;

import com.ig.eval.dao.CoffeeHouseDAO;
import com.ig.eval.service.InputValidatorService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CoffeeHouseConfiguration {

    @Bean
    public InputValidatorService inputValidatorService() {
        return new InputValidatorService();
    }

    @Bean
    public CoffeeHouseDAO coffeeHouseDAO() {
        return new CoffeeHouseDAO();
    }
}
