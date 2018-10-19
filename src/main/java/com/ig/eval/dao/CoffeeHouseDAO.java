package com.ig.eval.dao;

import com.ig.eval.exception.CoffeeHouseDAOException;
import com.ig.eval.model.CoffeeVariety;
import com.ig.eval.model.Customer;
import com.ig.eval.model.Order;
import com.ig.eval.model.OrderItem;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;

@Transactional
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
            throw new CoffeeHouseDAOException("Unable to retrieve/update information from/to DB while adding cutomer");
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
            throw new CoffeeHouseDAOException("Unable to retrieve/update information from/to DB while adding coffee variety");
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
        if (order == null) {
            throw new CoffeeHouseDAOException("order object not received");
        }
        Integer orderId;

        Integer customerId = getCustomerId(StringUtils.trim(order.getCustomerPhoneNumber()));

        try {
            orderId = jdbcTemplate.queryForObject("SELECT SEQ_ORDERS_ID.NEXTVAL", Integer.class);
            if (orderId == null) {
                throw new CoffeeHouseDAOException("Unable to retrieve orderId Id");
            }
            CollectionUtils.emptyIfNull(order.getItemList()).stream()
                    .filter(Objects::nonNull)
                    .forEach(orderItem ->
                            updateOrder(order, orderId, customerId, orderItem)
                    );
        } catch (DataAccessException dae) {
            throw new CoffeeHouseDAOException("Unable to retrieve/update information from/to DB while adding order");
        }
        return orderId;
    }

    private int updateOrder(Order order, Integer orderId, Integer customerId, OrderItem orderItem) {
        return jdbcTemplate.update("INSERT INTO ORDERS(ID, CUSTOMER_ID, COFFEE_VARIETY_ID, QUANTITY, ORDER_TIME) " +
                        "VALUES (?, ?, ?, ?, ?)",
                orderId,
                customerId,
                getCoffeeVarietyId(StringUtils.trim(orderItem.getCoffeeVarietyName())),
                orderItem.getQuantity(),
                order.getOrderTimeStamp());
    }

    private Integer getCoffeeVarietyId(String coffeeVarietyName) {
        try {
            return jdbcTemplate.queryForObject("SELECT ID FROM COFFEE_VARIETY WHERE LOWER(NAME) = ?",
                    new Object[]{StringUtils.lowerCase(coffeeVarietyName)}, Integer.class);
        } catch (DataAccessException dae) {
            throw new CoffeeHouseDAOException("Unable to retrieve coffeeVarietyName from DB");
        }
    }

    private Integer getCustomerId(String customerPhoneNumber) {
        try {
            return jdbcTemplate.queryForObject("SELECT ID FROM CUSTOMER WHERE PHONE_NUMBER = ?",
                    new Object[]{customerPhoneNumber}, Integer.class);
        } catch (DataAccessException dae) {
            throw new CoffeeHouseDAOException("Unable to retrieve customer Id from DB");
        }
    }

    public String getCustomerName(String customerPhoneNumber) {
        try {
            return jdbcTemplate.queryForObject("SELECT NAME FROM CUSTOMER WHERE PHONE_NUMBER = ?",
                    new Object[]{customerPhoneNumber}, String.class);
        } catch (DataAccessException dae) {
            throw new CoffeeHouseDAOException("Unable to retrieve customer name from DB");
        }
    }

    public Double getPrice(String coffeeVarietyName) {
        try {
            return jdbcTemplate.queryForObject("SELECT PRICE FROM COFFEE_VARIETY WHERE LOWER(NAME) = ?",
                    new Object[]{StringUtils.lowerCase(coffeeVarietyName)}, Double.class);
        } catch (DataAccessException dae) {
            throw new CoffeeHouseDAOException("Unable to retrieve PRICE from DB");
        }
    }

    public void adjustAvailability(Order order) {
        if (order == null) {
            throw new CoffeeHouseDAOException("order object not received");
        }
        try {
            CollectionUtils.emptyIfNull(order.getItemList())
                    .forEach(orderItem ->
                            jdbcTemplate.update("UPDATE COFFEE_VARIETY SET AVAILABLE_QUANTITY = (AVAILABLE_QUANTITY - ?) " +
                                            " WHERE ID = ?", orderItem.getQuantity(),
                                    getCoffeeVarietyId(orderItem.getCoffeeVarietyName()))
                    );
        } catch (DataAccessException dae) {
            throw new CoffeeHouseDAOException("Unable to retrieve/update information from/to DB while adjusting availability");
        }

    }
}

