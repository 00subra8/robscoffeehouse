package com.ig.eval.controller;

import com.ig.eval.configuration.CoffeeHouseConfiguration;
import com.ig.eval.dao.CoffeeHouseDAO;
import com.ig.eval.exception.CoffeeHouseInputException;
import com.ig.eval.model.Customer;
import com.ig.eval.service.InputValidatorService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Import({CoffeeHouseConfiguration.class, CoffeeHouseDAO.class})
public class ActionsController {

    private Logger logger = LoggerFactory.getLogger(ActionsController.class);

    @Autowired
    private InputValidatorService inputValidatorService;

    @Autowired
    private CoffeeHouseDAO coffeeHouseDAO;

    @RequestMapping(method = RequestMethod.POST, value = "/addCustomer")
    public String addCustomer(@RequestBody Customer customer) {

        if (customer == null) {
            String noInputExceptionMessage = "No Customer Input received";
            logAndThrowCoffeeHouseInputException(noInputExceptionMessage);
        }
        if (!inputValidatorService.validateCustomerName(customer.getCustomerName())) {
            String invalidCustomerNameMessage = "Invalid CustomerName: " + customer.getCustomerName();
            logAndThrowCoffeeHouseInputException(invalidCustomerNameMessage);
        }
        if (!inputValidatorService.validatePhoneNumber(customer.getPhoneNumber())) {
            String invalidPhoneNumberMessage = "Invalid phone number: " + customer.getPhoneNumber();
            logAndThrowCoffeeHouseInputException(invalidPhoneNumberMessage);
        }
        if (!inputValidatorService.isPhoneNumberUnique(customer.getPhoneNumber())) {
            String duplicatePhoneNumberMessage = "Duplicate phone number: " + customer.getPhoneNumber();
            logAndThrowCoffeeHouseInputException(duplicatePhoneNumberMessage);
        }

        Long customerId = coffeeHouseDAO.addCustomer(customer);

        return "Customer: " + customer.getCustomerName() + " added. Their Customer id is: " + customerId;

    }

    private String logAndThrowCoffeeHouseInputException(String inputExceptionMessage) {
        logger.error(inputExceptionMessage);
        throw new CoffeeHouseInputException(inputExceptionMessage);
    }


}
