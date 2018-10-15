package com.ig.eval.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.jdbc.core.JdbcTemplate;

public class PrepareCoffeeHouseDB {

//    SELECT * FROM CUSTOMER WHERE ID = 1

    @Autowired
    private JdbcTemplate jdbcTemplate;


    @Bean
    boolean setUpCoffeeHouseDb() {
        try {
            jdbcTemplate.execute("CREATE TABLE CUSTOMER(ID INT PRIMARY KEY, NAME VARCHAR(255), PHONE_NUMBER VARCHAR(255));");
            jdbcTemplate.execute("CREATE SEQUENCE SEQ_CUST_ID START WITH 1");

            jdbcTemplate.execute("CREATE TABLE COFFEE_VARIETY(ID INT PRIMARY KEY, COFFEE_NAME VARCHAR(255)," +
                    " DESCRIPTION VARCHAR(2550), SERVINGS_AVAILABLE INT);");
            jdbcTemplate.execute("CREATE SEQUENCE SEQ_COFFEE_VARIETY_ID START WITH 1");

            jdbcTemplate.execute("CREATE TABLE ORDER(ID INT PRIMARY KEY, CUSTOMER_ID INT," +
                    " COFFEE_VARIETY_ID INT, ORDER_TIME TIMESTAMP);");
            jdbcTemplate.execute("CREATE SEQUENCE SEQ_ORDER START WITH 1");
            jdbcTemplate.execute("ALTER TABLE ORDER ADD FOREIGN KEY (CUSTOMER_ID) CUSTOMER(ID)");
            jdbcTemplate.execute("ALTER TABLE ORDER ADD FOREIGN KEY (COFFEE_VARIETY_ID) COFFEE_VARIETY(ID)");


        } catch (Exception e) {
            //todo: redirect to global error page
        }
        return true;
    }


}
