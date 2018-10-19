package com.ig.eval.controller

import com.ig.eval.dao.CoffeeHouseDAO
import com.ig.eval.exception.CoffeeHouseInputException
import com.ig.eval.model.CoffeeVariety
import com.ig.eval.model.Customer
import com.ig.eval.model.Order
import com.ig.eval.model.OrderItem
import com.ig.eval.service.GenerateOrderReceiptService
import com.ig.eval.service.InputValidatorService
import org.apache.commons.lang3.StringUtils
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
        unit.generateOrderReceiptService = Mock(GenerateOrderReceiptService)
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

    def "Try to place null order"() {
        when:
        unit.processOrder(null)

        then:
        1 * unit.logger.error("No Order Input received")
        thrown(CoffeeHouseInputException)
    }

    @Unroll("Order is invalid for customer phone number:#customerPhoneNumber")
    def "Try to place order with blank customer phone number"(String customerPhoneNumber) {
        given:
        Order order = new Order()
        order.customerPhoneNumber = customerPhoneNumber

        when:
        unit.processOrder(order)

        then:
        1 * unit.logger.error("Customer phone number cannot be blank.")
        thrown(CoffeeHouseInputException)

        where:
        customerPhoneNumber << [null, "", " "]
    }

    def "Try to place order with non existent customer phone number"() {
        given:
        Order order = new Order()
        order.customerPhoneNumber = "non existent number"
        unit.inputValidatorService.isCustomerValid(order.customerPhoneNumber) >> false

        when:
        unit.processOrder(order)

        then:
        1 * unit.logger.error("Customer with Phone Number: " + order.getCustomerPhoneNumber()
                + " not in records. Please add new customer and reorder.")
        thrown(CoffeeHouseInputException)
    }

    @Unroll("Order is invalid for item list:#itemList")
    def "Try to place order with blank item list"(List<OrderItem> itemList) {
        given:
        Order order = new Order()
        order.customerPhoneNumber = "valid phone number"
        order.itemList = itemList
        unit.inputValidatorService.isCustomerValid(order.customerPhoneNumber) >> true

        when:
        unit.processOrder(order)

        then:
        1 * unit.logger.error("At least one item should be ordered.")
        thrown(CoffeeHouseInputException)

        where:
        itemList << [null, Collections.emptyList()]
    }

    def "Try to place order with invalid quantity"() {
        given:
        Order order = new Order()
        order.customerPhoneNumber = "valid phone number"
        OrderItem orderItem = new OrderItem()
        orderItem.quantity = -2
        List<OrderItem> itemList = new ArrayList<>()
        itemList.add(orderItem)
        order.itemList = itemList
        unit.inputValidatorService.isCustomerValid(order.customerPhoneNumber) >> true
        unit.inputValidatorService.isAvailabilityValid(orderItem.getQuantity()) >> false

        when:
        unit.processOrder(order)

        then:
        1 * unit.logger.error("Quantity Invalid: " + orderItem.getQuantity() +
                ". Quantity should be a valid integer.")
        thrown(CoffeeHouseInputException)
    }

    def "Try to place order with invalid variety"() {
        given:
        Order order = new Order()
        order.customerPhoneNumber = "valid phone number"
        OrderItem orderItem = new OrderItem()
        orderItem.quantity = 2
        orderItem.coffeeVarietyName = "invalid variety"
        List<OrderItem> itemList = new ArrayList<>()
        itemList.add(orderItem)
        order.itemList = itemList
        unit.inputValidatorService.isCustomerValid(order.customerPhoneNumber) >> true
        unit.inputValidatorService.isAvailabilityValid(orderItem.getQuantity()) >> true
        unit.inputValidatorService.isVarietyPresent(orderItem.getCoffeeVarietyName()) >> false

        when:
        unit.processOrder(order)

        then:
        1 * unit.logger.error("Item :" + orderItem.getCoffeeVarietyName() + " is not available in the menu.")
        thrown(CoffeeHouseInputException)
    }

    def "Try to place order with unavailable item variety"() {
        given:
        Order order = new Order()
        order.customerPhoneNumber = "valid phone number"
        OrderItem orderItem = new OrderItem()
        orderItem.quantity = 2000
        orderItem.coffeeVarietyName = "valid variety"
        List<OrderItem> itemList = new ArrayList<>()
        itemList.add(orderItem)
        order.itemList = itemList
        unit.inputValidatorService.isCustomerValid(order.customerPhoneNumber) >> true
        unit.inputValidatorService.isAvailabilityValid(orderItem.getQuantity()) >> true
        unit.inputValidatorService.isVarietyPresent(orderItem.getCoffeeVarietyName()) >> true
        unit.inputValidatorService.isItemAvailable(orderItem.getCoffeeVarietyName(), orderItem.getQuantity()) >> false

        when:
        unit.processOrder(order)

        then:
        1 * unit.logger.error("We do not have enough of Item: " + orderItem.getCoffeeVarietyName() +
                " to serve you. Please try reducing quantity or try some other item in our menu.")
        thrown(CoffeeHouseInputException)
    }

    def "Try to place a valid order"() {
        given:
        Order order = new Order()
        order.customerPhoneNumber = "valid phone number"
        OrderItem orderItem = new OrderItem()
        orderItem.quantity = 2
        orderItem.coffeeVarietyName = "valid variety"
        List<OrderItem> itemList = new ArrayList<>()
        itemList.add(orderItem)
        order.itemList = itemList
        unit.inputValidatorService.isCustomerValid(order.customerPhoneNumber) >> true
        unit.inputValidatorService.isAvailabilityValid(orderItem.getQuantity()) >> true
        unit.inputValidatorService.isVarietyPresent(orderItem.getCoffeeVarietyName()) >> true
        unit.inputValidatorService.isItemAvailable(orderItem.getCoffeeVarietyName(), orderItem.getQuantity()) >> true
        unit.coffeeHouseDAO.getCustomerName(StringUtils.trim(order.getCustomerPhoneNumber())) >> "customer name"
        unit.coffeeHouseDAO.addOrder(order) >> 12
        String receipt = "RECEIPT"
        unit.coffeeHouseDAO.adjustAvailability(order) >> null
        unit.generateOrderReceiptService.getReceipt(order) >> receipt

        expect:
        receipt == unit.processOrder(order)

    }

}
