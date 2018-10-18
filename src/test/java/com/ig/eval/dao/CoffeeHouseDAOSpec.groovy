package com.ig.eval.dao

import com.ig.eval.exception.CoffeeHouseDAOException
import com.ig.eval.model.CoffeeVariety
import com.ig.eval.model.Customer
import org.springframework.dao.DataAccessException
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.transaction.annotation.Transactional
import spock.lang.Specification

@Transactional
class CoffeeHouseDAOSpec extends Specification {
    private CoffeeHouseDAO unit

    void setup() {
        unit = new CoffeeHouseDAO()
        unit.jdbcTemplate = Mock(JdbcTemplate)
    }

    def "Add null customer"() {
        when:
        unit.addCustomer(null)

        then:
        thrown(CoffeeHouseDAOException)
    }

    def "Exception while retrieving sequence - customer"() {
        given:
        unit.jdbcTemplate.queryForObject("SELECT SEQ_CUST_ID.NEXTVAL", Long.class) >> null

        when:
        unit.addCustomer(new Customer())

        then:
        thrown(CoffeeHouseDAOException)
    }

    def "Adding a customer"() {
        given:
        def customer = new Customer()
        customer.customerName = "Subramanian"
        customer.phoneNumber = "1234567890"
        def customerId = new Long(5)
        unit.jdbcTemplate.queryForObject("SELECT SEQ_CUST_ID.NEXTVAL", Long.class) >> customerId
        unit.jdbcTemplate.update("INSERT INTO CUSTOMER(ID, NAME, PHONE_NUMBER) VALUES (?, ?, ?)", customerId,
                customer.getCustomerName(), customer.getPhoneNumber()) >> null

        when:
        unit.addCustomer(new Customer())

        then:
        notThrown(CoffeeHouseDAOException)
    }

    def "get All Phone numbers"() {
        given:
        def phoneNumberList = ["1234567890", "+441234557799"]
        unit.jdbcTemplate.queryForList("SELECT PHONE_NUMBER FROM CUSTOMER", String.class) >> phoneNumberList

        when:
        def result = unit.getAllPhoneNumbers()

        then:
        result == phoneNumberList
    }

    def "get All Variety Names"() {
        given:
        def varietyList = ["Mocha", "Cappuccino"]
        unit.jdbcTemplate.queryForList("SELECT NAME FROM COFFEE_VARIETY", String.class) >> varietyList

        when:
        def result = unit.getAllVarietyNames()

        then:
        result == varietyList
    }

    def "Add null coffee variety"() {
        when:
        unit.addCoffeeVariety(null)

        then:
        thrown(CoffeeHouseDAOException)
    }

    def "Exception while retrieving sequence - coffee variety"() {
        given:
        unit.jdbcTemplate.queryForObject("SELECT SEQ_COFFEE_VARIETY_ID.NEXTVAL", Long.class) >> null

        when:
        unit.addCoffeeVariety(new CoffeeVariety())

        then:
        thrown(CoffeeHouseDAOException)
    }

    def "Adding a coffee variety"() {
        given:
        def coffeeVariety = new CoffeeVariety()
        coffeeVariety.name = "Latte"
        coffeeVariety.description = "Milk and Milk"
        coffeeVariety.availableQuantity = "65"
        coffeeVariety.price = "4.50"
        def varietyId = new Long(5)
        unit.jdbcTemplate.queryForObject("SELECT SEQ_COFFEE_VARIETY_ID.NEXTVAL", Long.class) >> varietyId
        unit.jdbcTemplate.update("INSERT INTO COFFEE_VARIETY(ID, NAME, DESCRIPTION, AVAILABLE_QUANTITY, PRICE) " +
                "VALUES (?, ?, ?, ?, ?)", varietyId, coffeeVariety.name, coffeeVariety.description,
                Integer.valueOf(coffeeVariety.availableQuantity), Double.valueOf(coffeeVariety.price)) >> null

        when:
        unit.addCoffeeVariety(coffeeVariety)

        then:
        notThrown(CoffeeHouseDAOException)
    }

}
