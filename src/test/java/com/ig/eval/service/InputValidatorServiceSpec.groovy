package com.ig.eval.service

import com.ig.eval.dao.CoffeeHouseDAO
import com.ig.eval.exception.CoffeeHouseInputException
import org.slf4j.Logger
import spock.lang.Specification
import spock.lang.Unroll

class InputValidatorServiceSpec extends Specification {

    private InputValidatorService unit

    void setup() {
        unit = new InputValidatorService()
        unit.logger = Mock(Logger)
        unit.coffeeHouseDAO = Mock(CoffeeHouseDAO)
    }

    @Unroll("For customer name #customerName expected result is #result")
    def "Validate Customer Name"(String customerName, boolean result) {

        expect:
        unit.validateCustomerName(customerName) == result

        where:
        customerName                    | result
        "Subramanian"                   | true
        "Subramanian "                  | true
        "Subramanian Meenakshisundaram" | true
        "11234"                         | false
        "Subramanian 11234"             | false
        "Subra34234manian"              | false
    }

    @Unroll("For customer name #customerName expected result is false")
    def "Validate Customer Name for Null and Empty inputs"(String customerName) {

        when:
        def result = unit.validateCustomerName(customerName)

        then:
        !result
        1 * unit.logger.error("customerName is blank - input validation fail")


        where:
        customerName << ["", null, " "]
    }

    @Unroll("For phone number #phoneNumber expected result is #result")
    def "Validate Phone Number"(String phoneNumber, boolean result) {
        expect:
        unit.validatePhoneNumber(phoneNumber) == result

        where:
        phoneNumber            | result
        "1234567890"           | true
        "123-456-7890"         | true
        "123-456-7890 x1234"   | true
        "123-456-7890 ext1234" | true
        "(123)-456-7890"       | true
        "123.456.7890"         | true
        "123 456 7890"         | true
        "+447459453234"        | true
        "12345678900"          | false
        "Phone"                | false
    }

    @Unroll("For phone number #phoneNumber expected result is false")
    def "Validate Phone Number with null or empty values"(String phoneNumber) {
        when:
        def result = unit.validatePhoneNumber(phoneNumber)

        then:
        !result
        1 * unit.logger.error("phoneNumber is blank - input validation fail")

        where:
        phoneNumber << ["", null, " "]
    }

    @Unroll("For phone number #phoneNumber expected result is #result")
    def "Check if phone number of new customer is unique"(def phoneNumberDBList, String phoneNumber, boolean result) {
        given:
        unit.coffeeHouseDAO = Mock(CoffeeHouseDAO)
        unit.coffeeHouseDAO.getAllPhoneNumbers() >> phoneNumberDBList

        expect:
        unit.isPhoneNumberUnique(phoneNumber) == result

        where:
        phoneNumberDBList                             | phoneNumber     | result
        ["1234567890", "+441234567890", "2345678901"] | "+441212121212" | true
        ["1234567890", "+441234567890", "2345678901"] | "1234567890"    | false
        []                                            | "1234567890"    | true
        null                                          | "1234567890"    | true
    }

    @Unroll("For coffee variety #variety expected result is #result")
    def "Check if new coffee variety is unique"(def varietyDBList, String variety, boolean result) {
        given:
        unit.coffeeHouseDAO = Mock(CoffeeHouseDAO)
        unit.coffeeHouseDAO.getAllVarietyNames() >> varietyDBList

        expect:
        unit.isVarietyUnique(variety) == result

        where:
        varietyDBList              | variety | result
        ["Cafe", "Mocha", "Latte"] | "Chai"  | true
        ["Cafe", "Mocha", "Latte"] | "Mocha" | false
        []                         | "Latte" | true
        null                       | "Mocha" | true
    }

    @Unroll("For available quantity #availableQuantity the validity is #validity")
    def "Check if available quantity is valid"(String availableQuantity, boolean validity) {
        expect:
        validity == unit.isAvailabilityValid(availableQuantity)

        where:
        availableQuantity | validity
        "34"              | true
        "0"               | true
        "300"             | true
        "-1"              | false
        "-150"            | false
    }

    @Unroll("For available quantity #availableQuantity Exception is thrown")
    def "Check if Exception is thrown for invalid availability"(String availableQuantity) {
        when:
        unit.isAvailabilityValid(availableQuantity)

        then:
        1 * unit.logger.error("Available quantity not a number")
        thrown(CoffeeHouseInputException)

        where:
        availableQuantity << [null, "", " ", "dfsd"]
    }

    @Unroll("For price #price the validity is #validity")
    def "Check if price is valid"(String price, boolean validity) {
        expect:
        unit.isPriceValid(price) == validity

        where:
        price    | validity
        "34"     | true
        "0"      | true
        "300"    | true
        "-1"     | false
        "-1.5"   | false
        "1.50"   | true
        "15.10"  | true
        "150.55" | true
        "7.555"  | false
        "7.0"    | true
        "7.00"   | true
        "7."     | true
    }

    @Unroll("For price #price Exception is thrown")
    def "Check if Exception is thrown for invalid price"(String price) {
        when:
        unit.isPriceValid(price)

        then:
        1 * unit.logger.error("Price not a number")
        thrown(CoffeeHouseInputException)

        where:
        price << [null, "", " ", "dfsd", "4.5J"]
    }

    def "check for non available variety"() {
        given:
        def varietyName = "Invalid variety"
        unit.coffeeHouseDAO.getAllVarietyNames() >> ["var1", "var2"]

        expect:
        !unit.isItemAvailable(varietyName, "any quantity")

    }

    @Unroll("For quantity: #quantity, with available quantity: #availableQuantity the expected result is: #result")
    def "check for available variety with invalid/valid quantity"(String quantity, int availableQuantity, boolean result) {
        given:
        def varietyName = "valid variety"
        unit.coffeeHouseDAO.getAllVarietyNames() >> ["var1", "var2", varietyName]
        unit.coffeeHouseDAO.getAvailableQuantity(varietyName) >> availableQuantity

        expect:
        unit.isItemAvailable(varietyName, quantity) == result

        where:
        quantity | availableQuantity | result
        "5"      | 50                | true
        "5"      | 4                 | false
    }

    @Unroll("For invalid quantity: #quantity exception is thrown")
    def "check for available variety with invalid quantity"(String quantity) {
        given:
        def varietyName = "valid variety"
        unit.coffeeHouseDAO.getAllVarietyNames() >> ["var1", "var2", varietyName]
        unit.coffeeHouseDAO.getAvailableQuantity(varietyName) >> 300

        when:
        unit.isItemAvailable(varietyName, quantity)

        then:
        1 * unit.logger.error("quantity not a number")
        thrown(CoffeeHouseInputException)

        where:
        quantity << ["invalid", null, "", " "]
    }

    @Unroll("For variety: #variety expected result is #result")
    def "Check if variety is present"(String variety, boolean result) {
        given:
        unit.coffeeHouseDAO.getAllVarietyNames() >> ["var1", "var2", "var3"]

        expect:
        unit.isVarietyPresent(variety) == result

        where:
        variety | result
        "var1"  | true
        "var2"  | true
        "var4"  | false
        "45"    | false
        null    | false

    }

    @Unroll("For customer with phone number: #customerPhoneNumber expected result is #result")
    def "Check if customer is valid"(String customerPhoneNumber, boolean result) {
        given:
        unit.coffeeHouseDAO.getAllPhoneNumbers() >> ["123", "456", "789"]

        expect:
        unit.isCustomerValid(customerPhoneNumber) == result

        where:
        customerPhoneNumber | result
        "123"               | true
        "456"               | true
        "var4"              | false
        "45"                | false
        null                | false

    }

}
