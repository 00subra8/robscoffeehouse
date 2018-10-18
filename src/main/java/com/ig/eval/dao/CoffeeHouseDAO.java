package com.ig.eval.dao;

import com.ig.eval.exception.CoffeeHouseDAOException;
import com.ig.eval.model.CoffeeVariety;
import com.ig.eval.model.Customer;
import com.ig.eval.model.Order;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;

import java.sql.Timestamp;
import java.time.LocalDateTime;
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
            jdbcTemplate.update("INSERT INTO COFFEE_VARIETY(ID, NAME, DESCRIPTION, AVAILABLE_QUANTITY, PRICE) " +
                            "VALUES (?, ?, ?, ?, ?)", varietyId, coffeeVariety.getName(), coffeeVariety.getDescription(),
                    Integer.valueOf(coffeeVariety.getAvailableQuantity()), Double.valueOf(coffeeVariety.getPrice()));
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

    public Integer getAvailableQuantity(String varietyName) {
        try {
            return jdbcTemplate.queryForObject("SELECT AVAILABLE_QUANTITY FROM COFFEE_VARIETY WHERE NAME = ?",
                    new Object[]{varietyName}, Integer.class);
        } catch (DataAccessException dae) {
            throw new CoffeeHouseDAOException("Unable to retrieve information from DB");
        }
    }

    public int addOrder(Order order) {
//        if (order == null) {
//            throw new CoffeeHouseDAOException("order object not received");
//        }
//        Long orderId;
//        try {
//            orderId = jdbcTemplate.queryForObject("SELECT SEQ_ORDERS.NEXTVAL", Long.class);
//            if (orderId == null) {
//                throw new CoffeeHouseDAOException("Unable to retrieve orderId Id");
//            }
//            CollectionUtils.emptyIfNull(order.getOrderItemList())
//            jdbcTemplate.update("INSERT INTO ORDERS(ID, CUSTOMER_ID, COFFEE_VARIETY_ID, QUANTITY, ORDER_TIME) " +
//                            "VALUES (?, ?, ?, ?, ?)", varietyId, coffeeVariety.getName(), coffeeVariety.getDescription(),
//                    Timestamp.valueOf(LocalDateTime.now()));
//        } catch (DataAccessException dae) {
//            throw new CoffeeHouseDAOException("Unable to retrieve/update information from/to DB");
//        }
//        return varietyId;
        return 0;
    }
}

