package com.ig.eval.configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.jdbc.core.JdbcTemplate;

public class PrepareCoffeeHouseDB {

//INSERT INTO CUSTOMER VALUES (SEQ_CUST_ID.NEXTVAL, 'ROB', '07404582721')
//    SELECT * FROM CUSTOMER WHERE ID = 1
//SELECT SEQ_CUST_ID.NEXTVAL

    @Autowired
    private JdbcTemplate jdbcTemplate;


    @Bean
    boolean setUpCoffeeHouseDb() {
        try {
            jdbcTemplate.execute("CREATE TABLE CUSTOMER(ID INT PRIMARY KEY, NAME VARCHAR(255), PHONE_NUMBER VARCHAR(255));");
            jdbcTemplate.execute("CREATE SEQUENCE SEQ_CUST_ID START WITH 1");
        } catch (Exception e) {
            //todo: redirect to global error page
        }
        return true;
    }


}
