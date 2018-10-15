package com.ig.eval.dao;

import com.ig.eval.exception.CoffeeHouseDAOException;
import com.ig.eval.model.Customer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;

@Configuration
@PropertySource("classpath:application.properties")
public class CoffeeHouseDAO {
    @Autowired
    public DataSource dataSource;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public Long addCustomer(Customer customer) {
        if (customer == null) {
            throw new CoffeeHouseDAOException("Customer object not received");
        }
        Long customerId = null;
        try {
            customerId = jdbcTemplate.queryForObject("SELECT SEQ_CUST_ID.NEXTVAL", new Object[]{}, Long.class);
            if (customerId == null) {
                throw new CoffeeHouseDAOException("Unable to retrieve customer id");
            }
            jdbcTemplate.update("INSERT INTO CUSTOMER(ID, NAME, PHONE_NUMBER) VALUES (?, ?, ?)", customerId,
                    customer.getCustomerName(), customer.getPhoneNumber());
        } catch (DataAccessException dae) {
            //todo:redirect to error page
        }
        return customerId;
    }
}
