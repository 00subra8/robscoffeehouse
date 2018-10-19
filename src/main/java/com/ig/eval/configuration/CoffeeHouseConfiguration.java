package com.ig.eval.configuration;

import com.ig.eval.dao.CoffeeHouseDAO;
import com.ig.eval.model.ReceiptBuilder;
import com.ig.eval.service.GenerateExcelReportService;
import com.ig.eval.service.GenerateOrderReceiptService;
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
    public GenerateOrderReceiptService generateOrderReceiptService() {
        return new GenerateOrderReceiptService();
    }

    @Bean
    public CoffeeHouseDAO coffeeHouseDAO() {
        return new CoffeeHouseDAO();
    }

    @Bean
    public ReceiptBuilder receiptBuilder() {
        return new ReceiptBuilder();
    }

    @Bean
    public GenerateExcelReportService generateExcelReportService() {
        return new GenerateExcelReportService();
    }

}

