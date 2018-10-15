package com.ig.eval.controller;

import com.ig.eval.configuration.ApplicationProperties;
import com.ig.eval.configuration.CoffeeHouseConfiguration;
import com.ig.eval.dao.CoffeeHouseDAO;
import com.ig.eval.dao.PrepareCoffeeHouseDB;
import com.ig.eval.model.Customer;
import com.ig.eval.service.InputValidatorService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;
import org.springframework.web.bind.annotation.*;

@RestController
@Import({PrepareCoffeeHouseDB.class, CoffeeHouseConfiguration.class, CoffeeHouseDAO.class})
public class ActionsController {

    private Logger logger = LoggerFactory.getLogger(ActionsController.class);


    @Autowired
    private ApplicationProperties applicationProperties;

    @Autowired
    private InputValidatorService inputValidatorService;

    @Autowired
    private CoffeeHouseDAO coffeeHouseDAO;

    @RequestMapping(method = RequestMethod.POST, value = "/addCustomer")
    public String addCustomer(@RequestBody Customer customer) {
        if (customer == null) {
            //todo: redirect to error page
        }

        if (!inputValidatorService.validateCustomerName(customer.getCustomerName())) {
            logger.error("Invalid CustomerName: " + customer.getCustomerName());
            //todo: redirect error page with this message
        }
        if (!inputValidatorService.validatePhoneNumber(customer.getPhoneNumber())) {
            logger.error("Invalid phone number: " + customer.getPhoneNumber());
            //todo: redirect error page with this message
        }

        Long customerId = coffeeHouseDAO.addCustomer(customer);


        return "Customer: " + customer.getCustomerName() + " added. Their Customer id is: " + customerId;
    }


}
