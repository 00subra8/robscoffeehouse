package com.ig.eval.dao;

import com.ig.eval.exception.CoffeeHouseDAOException;
import com.ig.eval.model.CoffeeVariety;
import com.ig.eval.model.Customer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.List;

@Configuration
public class CoffeeHouseDAO {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public Long addCustomer(Customer customer) {
        if (customer == null) {
            throw new CoffeeHouseDAOException("Customer object not received");
        }
        Long customerId;
        try {
            customerId = jdbcTemplate.queryForObject("SELECT SEQ_CUST_ID.NEXTVAL", Long.class);
            if (customerId == null) {
                throw new CoffeeHouseDAOException("Unable to retrieve customer id");
            }
            jdbcTemplate.update("INSERT INTO CUSTOMER(ID, NAME, PHONE_NUMBER) VALUES (?, ?, ?)", customerId,
                    customer.getCustomerName(), customer.getPhoneNumber());
        } catch (DataAccessException dae) {
            throw new CoffeeHouseDAOException("Unable to retrieve/update information from/to DB");
        }
        return customerId;
    }

    public List<String> getAllPhoneNumbers() {
        try {
            return jdbcTemplate.queryForList("SELECT PHONE_NUMBER FROM CUSTOMER", String.class);
        } catch (DataAccessException dae) {
            throw new CoffeeHouseDAOException("Unable to retrieve information from DB");
        }
    }

    public Long addCoffeeVariety(CoffeeVariety coffeeVariety) {
        if (coffeeVariety == null) {
            throw new CoffeeHouseDAOException("coffeeVariety object not received");
        }
        Long varietyId;
        try {
            varietyId = jdbcTemplate.queryForObject("SELECT SEQ_COFFEE_VARIETY_ID.NEXTVAL", Long.class);
            if (varietyId == null) {
                throw new CoffeeHouseDAOException("Unable to retrieve variety Id");
            }
            jdbcTemplate.update("INSERT INTO COFFEE_VARIETY(ID, NAME, DESCRIPTION, AVAILABLE_QUANTITY) " +
                            "VALUES (?, ?, ?, ?)", varietyId, coffeeVariety.getName(), coffeeVariety.getDescription(),
                    Integer.valueOf(coffeeVariety.getAvailableQuantity()));
        } catch (DataAccessException dae) {
            throw new CoffeeHouseDAOException("Unable to retrieve/update information from/to DB");
        }
        return varietyId;
    }

    public List<String> getAllVarietyNames() {
        try {
            return jdbcTemplate.queryForList("SELECT NAME FROM COFFEE_VARIETY", String.class);
        } catch (DataAccessException dae) {
            throw new CoffeeHouseDAOException("Unable to retrieve information from DB");
        }
    }
}

