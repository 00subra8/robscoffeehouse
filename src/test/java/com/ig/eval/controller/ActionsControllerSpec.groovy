package com.ig.eval.controller

import com.ig.eval.dao.CoffeeHouseDAO
import com.ig.eval.exception.CoffeeHouseInputException
import com.ig.eval.model.Customer
import com.ig.eval.service.InputValidatorService
import org.slf4j.Logger
import spock.lang.Specification

class ActionsControllerSpec extends Specification {

    private ActionsController unit

    void setup() {
        unit = new ActionsController()
        unit.logger = Mock(Logger)
        unit.inputValidatorService = Mock(InputValidatorService)
        unit.coffeeHouseDAO = Mock(CoffeeHouseDAO)
    }

    def "Try to add null Customer"() {
        when:
        unit.addCustomer(null)

        then:
        1 * unit.logger.error("No Customer Input received")
        thrown(CoffeeHouseInputException)
    }

    def "Try to add Customer with Invalid name"() {
        given:
        Customer customer = new Customer()
        customer.setCustomerName("123455");
        unit.inputValidatorService.validateCustomerName(customer.customerName) >> false

        when:
        unit.addCustomer(customer)

        then:
        1 * unit.logger.error("Invalid CustomerName: " + customer.customerName)
        thrown(CoffeeHouseInputException)
    }

    def "Try to add Customer with Invalid phone number"() {
        given:
        Customer customer = new Customer()
        customer.setCustomerName("Subramanian")
        customer.setPhoneNumber("Subramanian")
        unit.inputValidatorService.validateCustomerName(customer.customerName) >> true
        unit.inputValidatorService.validatePhoneNumber(customer.phoneNumber) >> false

        when:
        unit.addCustomer(customer)

        then:
        1 * unit.logger.error("Invalid phone number: " + customer.phoneNumber)
        thrown(CoffeeHouseInputException)
    }

    def "Try to add Customer with duplicate phone number"() {
        given:
        Customer customer = new Customer()
        customer.setCustomerName("Subramanian")
        customer.setPhoneNumber("1234567890")
        unit.inputValidatorService.validateCustomerName(customer.customerName) >> true
        unit.inputValidatorService.validatePhoneNumber(customer.phoneNumber) >> true
        unit.inputValidatorService.isPhoneNumberUnique(customer.phoneNumber) >> false

        when:
        unit.addCustomer(customer)

        then:
        1 * unit.logger.error("Duplicate phone number: " + customer.phoneNumber)
        thrown(CoffeeHouseInputException)
    }

    def "Try to add Valid Customer"() {
        given:
        Customer customer = new Customer()
        customer.setCustomerName("Subramanian")
        customer.setPhoneNumber("2234567890")
        unit.inputValidatorService.validateCustomerName(customer.customerName) >> true
        unit.inputValidatorService.validatePhoneNumber(customer.phoneNumber) >> true
        unit.inputValidatorService.isPhoneNumberUnique(customer.phoneNumber) >> true
        def customerId = 10
        unit.coffeeHouseDAO.addCustomer(customer) >> new Long(customerId)

        when:
        def response = unit.addCustomer(customer)

        then:
        notThrown(CoffeeHouseInputException)
        response == "Customer: " + customer.getCustomerName() + " added. Their Customer id is: " + customerId
    }

}
