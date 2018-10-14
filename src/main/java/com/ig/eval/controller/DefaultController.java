package com.ig.eval.controller;


import com.ig.eval.configuration.ApplicationProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class DefaultController {

    @Autowired
    private ApplicationProperties applicationProperties;

    @RequestMapping("/")
    public String displayWelcomeMessage(){
        return applicationProperties.getWelcome();
    }

    //todo:add global error handler
}
