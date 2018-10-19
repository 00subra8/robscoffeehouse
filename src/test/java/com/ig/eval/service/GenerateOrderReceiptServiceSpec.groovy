package com.ig.eval.service

import com.ig.eval.exception.CoffeeHouseInputException
import com.ig.eval.model.Order
import spock.lang.Specification
import spock.lang.Unroll

class GenerateOrderReceiptServiceSpec extends Specification {
    private GenerateOrderReceiptService unit

    void setup() {
        unit = new GenerateOrderReceiptService();
    }

    @Unroll("For invalid order: #order an exception is thrown")
    def "Invalid order test"(Order order) {
        when:
        unit.getReceipt(order)

        then:
        thrown(CoffeeHouseInputException)

        where:
        order << [null, new Order()]
    }

}
