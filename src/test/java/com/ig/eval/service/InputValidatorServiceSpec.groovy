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
    def "Check if Exception is thrown for invalid available"(String availableQuantity) {
        when:
        unit.isAvailabilityValid(availableQuantity)

        then:
        1 * unit.logger.error("Available quantity not a number")
        thrown(CoffeeHouseInputException)

        where:
        availableQuantity << [null, "", " ", "dfsd"]
    }

}
