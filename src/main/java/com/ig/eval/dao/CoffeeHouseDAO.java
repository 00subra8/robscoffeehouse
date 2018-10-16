package com.ig.eval.dao;

import com.ig.eval.exception.CoffeeHouseDAOException;
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
            throw new CoffeeHouseDAOException("Unable to retrieve information from DB");
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
}
