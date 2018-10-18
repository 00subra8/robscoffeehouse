package com.ig.eval.controller

import com.ig.eval.dao.CoffeeHouseDAO
import com.ig.eval.exception.CoffeeHouseInputException
import com.ig.eval.model.CoffeeVariety
import com.ig.eval.model.Customer
import com.ig.eval.service.InputValidatorService
import org.slf4j.Logger
import spock.lang.Specification
import spock.lang.Unroll

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

    def "Try to add null Coffee Variety"() {
        when:
        unit.addCoffeeVariety(null)

        then:
        1 * unit.logger.error("No Coffee variety Input received")
        thrown(CoffeeHouseInputException)
    }

    @Unroll("CoffeeVariety name varietyName: #varietyName is invalid")
    def "Try to add Coffee Variety with Invalid name"(String varietyName) {
        given:
        CoffeeVariety coffeeVariety = new CoffeeVariety()
        coffeeVariety.setName(varietyName)

        when:
        unit.addCoffeeVariety(coffeeVariety)

        then:
        1 * unit.logger.error("Blank Coffee Variety name ")
        thrown(CoffeeHouseInputException)

        where:
        varietyName << [null, "", " "]
    }

    @Unroll("CoffeeVariety availableQuantity #availableQuantity is invalid")
    def "Try to add Coffee Variety with Invalid available quantity"(String availableQuantity) {
        given:
        CoffeeVariety coffeeVariety = new CoffeeVariety()
        coffeeVariety.setName("Filter Coffee")
        coffeeVariety.setDescription("Filter Coffee")
        coffeeVariety.setAvailableQuantity(availableQuantity)
        unit.inputValidatorService.isVarietyUnique(coffeeVariety.getName()) >> true
        unit.inputValidatorService.isAvailabilityValid(coffeeVariety.getAvailableQuantity()) >> false

        when:
        unit.addCoffeeVariety(coffeeVariety)

        then:
        1 * unit.logger.error("Invalid Availability: " + availableQuantity + ". Available quantity should be an integer between 0 and 300")
        thrown(CoffeeHouseInputException)

        where:
        availableQuantity << [null, "", " ", "-1", "sdaa", "301"]
    }

    @Unroll("CoffeeVariety price #price is invalid")
    def "Try to add Coffee Variety with Invalid price"(String price) {
        given:
        CoffeeVariety coffeeVariety = new CoffeeVariety()
        coffeeVariety.setName("Filter Coffee")
        coffeeVariety.setDescription("Filter Coffee")
        coffeeVariety.setAvailableQuantity("45")
        coffeeVariety.setPrice(price)
        unit.inputValidatorService.isVarietyUnique(coffeeVariety.getName()) >> true
        unit.inputValidatorService.isAvailabilityValid(coffeeVariety.getAvailableQuantity()) >> true
        unit.inputValidatorService.isPriceValid(coffeeVariety.getPrice()) >> false

        when:
        unit.addCoffeeVariety(coffeeVariety)

        then:
        1 * unit.logger.error("Invalid Price: " + coffeeVariety.getPrice() + ". " +
                "Price should be a positive double like : 3.50, 100.00, 20")
        thrown(CoffeeHouseInputException)

        where:
        price << [null, "", " ", "-1", "sdaa", "30.5s"]
    }

    def "Try to add duplicate Coffee Variety name"() {
        given:
        CoffeeVariety coffeeVariety = new CoffeeVariety()
        coffeeVariety.setName("Cappuccino")
        unit.inputValidatorService.isVarietyUnique(coffeeVariety.getName()) >> false

        when:
        unit.addCoffeeVariety(coffeeVariety)

        then:
        1 * unit.logger.error("Duplicate variety name: " + coffeeVariety.getName())
        thrown(CoffeeHouseInputException)
    }

    def "Try to add valid Coffee Variety "() {
        given:
        CoffeeVariety coffeeVariety = new CoffeeVariety()
        coffeeVariety.name = "Chai Latte"
        coffeeVariety.description = "Indian Chai with a british tinge"
        coffeeVariety.availableQuantity = "75"
        coffeeVariety.price = "7.75"
        unit.inputValidatorService.isVarietyUnique(coffeeVariety.getName()) >> true
        unit.inputValidatorService.isAvailabilityValid(coffeeVariety.getAvailableQuantity()) >> true
        unit.inputValidatorService.isPriceValid(coffeeVariety.getPrice()) >> true
        def varietyId = 12
        unit.coffeeHouseDAO.addCoffeeVariety(coffeeVariety) >> varietyId

        when:
        def result = unit.addCoffeeVariety(coffeeVariety)

        then:
        result == "Coffee Variety " + coffeeVariety.getName() + " added successfully to the menu. Menu item number is: " + varietyId
    }

}
