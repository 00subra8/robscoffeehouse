package com.ig.eval.controller;

import com.ig.eval.configuration.CoffeeHouseConfiguration;
import com.ig.eval.dao.CoffeeHouseDAO;
import com.ig.eval.exception.CoffeeHouseInputException;
import com.ig.eval.model.CoffeeVariety;
import com.ig.eval.model.Customer;
import com.ig.eval.model.Order;
import com.ig.eval.model.OrderItem;
import com.ig.eval.service.GenerateOrderReceiptService;
import com.ig.eval.service.InputValidatorService;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Objects;

@RestController
@Import({CoffeeHouseConfiguration.class, CoffeeHouseDAO.class})
public class ActionsController {

    private Logger logger = LoggerFactory.getLogger(ActionsController.class);

    @Autowired
    private InputValidatorService inputValidatorService;

    @Autowired
    private GenerateOrderReceiptService generateOrderReceiptService;

    @Autowired
    private CoffeeHouseDAO coffeeHouseDAO;

    @RequestMapping(method = RequestMethod.POST, value = "/addCustomer")
    public String addCustomer(@RequestBody Customer customer) {

        if (customer == null) {
            logAndThrowCoffeeHouseInputException("No Customer Input received");
        }
        if (!inputValidatorService.validateCustomerName(customer.getCustomerName())) {
            logAndThrowCoffeeHouseInputException("Invalid CustomerName: " + customer.getCustomerName());
        }
        if (!inputValidatorService.validatePhoneNumber(customer.getPhoneNumber())) {
            logAndThrowCoffeeHouseInputException("Invalid phone number: " + customer.getPhoneNumber());
        }
        if (!inputValidatorService.isPhoneNumberUnique(customer.getPhoneNumber())) {
            logAndThrowCoffeeHouseInputException("Duplicate phone number: " + customer.getPhoneNumber());
        }

        Long customerId = coffeeHouseDAO.addCustomer(customer);

        return "Customer: " + customer.getCustomerName() + " added. Their Customer id is: " + customerId;

    }

    @RequestMapping(method = RequestMethod.POST, value = "/addVariety")
    public String addCoffeeVariety(@RequestBody CoffeeVariety coffeeVariety) {
        if (coffeeVariety == null) {
            logAndThrowCoffeeHouseInputException("No Coffee variety Input received");
        }

        if (StringUtils.isBlank(coffeeVariety.getName())) {
            logAndThrowCoffeeHouseInputException("Blank Coffee Variety name ");
        }

        if (!inputValidatorService.isVarietyUnique(coffeeVariety.getName())) {
            logAndThrowCoffeeHouseInputException("Duplicate variety name: " + coffeeVariety.getName());
        }

        if (!inputValidatorService.isAvailabilityValid(coffeeVariety.getAvailableQuantity())) {
            logAndThrowCoffeeHouseInputException("Invalid Availability: " + coffeeVariety.getAvailableQuantity() + ". " +
                    "Available quantity should be an integer between 0 and 300");
        }

        if (!inputValidatorService.isPriceValid(coffeeVariety.getPrice())) {
            logAndThrowCoffeeHouseInputException("Invalid Price: " + coffeeVariety.getPrice() + ". " +
                    "Price should be a positive double like : 3.50, 100.00, 20");
        }

        Long varietyId = coffeeHouseDAO.addCoffeeVariety(coffeeVariety);

        return "Coffee Variety " + coffeeVariety.getName() + " added successfully to the menu. Menu item number is: " + varietyId;
    }

    @RequestMapping(method = RequestMethod.POST, value = "/order")
    public String processOrder(@RequestBody Order order) {
        if (order == null) {
            logAndThrowCoffeeHouseInputException("No Order Input received");
        }

        if (StringUtils.isBlank(order.getCustomerPhoneNumber())) {
            logAndThrowCoffeeHouseInputException("Customer phone number cannot be blank.");
        }

        List<OrderItem> orderItemList = order.getOrderItemList();
        if (CollectionUtils.isEmpty(orderItemList)) {
            logAndThrowCoffeeHouseInputException("At least one item should be ordered.");
        }

        orderItemList.stream()
                .filter(Objects::nonNull)
                .forEach(this::checkValidVariety);

        int orderId = coffeeHouseDAO.addOrder(order);


        return null;


    }

    private void checkValidVariety(OrderItem orderItem) {
        if (!inputValidatorService.isVarietyPresent(orderItem.getCoffeeVarietyName())) {
            logAndThrowCoffeeHouseInputException("Item :" + orderItem.getCoffeeVarietyName() + " is not available in the menu.");
        }

        if (!inputValidatorService.isItemAvailable(orderItem.getCoffeeVarietyName(), orderItem.getQuantity())) {
            logAndThrowCoffeeHouseInputException("We do not have enough of Item :" + orderItem.getCoffeeVarietyName() +
                    " to serve you. Please try reducing quantity or try some other item in our menu.");
        }
    }

    private void logAndThrowCoffeeHouseInputException(String inputExceptionMessage) {
        logger.error(inputExceptionMessage);
        throw new CoffeeHouseInputException(inputExceptionMessage);
    }

}
