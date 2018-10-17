package com.ig.eval.dao

import com.ig.eval.exception.CoffeeHouseDAOException
import com.ig.eval.model.Customer
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

    def "Exception while retrieving sequence"() {
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

}
