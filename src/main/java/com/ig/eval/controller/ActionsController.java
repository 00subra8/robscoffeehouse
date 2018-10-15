package com.ig.eval.controller;

import com.ig.eval.configuration.ApplicationProperties;
import com.ig.eval.configuration.CoffeeHouseConfiguration;
import com.ig.eval.dao.CoffeeHouseDAO;
import com.ig.eval.dao.PrepareCoffeeHouseDB;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Import({PrepareCoffeeHouseDB.class, CoffeeHouseConfiguration.class, CoffeeHouseDAO.class})
public class ActionsController {

    @Autowired
    private ApplicationProperties applicationProperties;

    @PostMapping("/addCustomer/{customerName}/{phoneNumber}")
    public String addCustomer(@PathVariable String customerName, @PathVariable String phoneNumber) {
        return null;
    }


}
