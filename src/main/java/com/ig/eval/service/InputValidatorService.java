package com.ig.eval.service;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class InputValidatorService {

    private Logger logger = LoggerFactory.getLogger(InputValidatorService.class);


    public boolean validateCustomerName(String customerName) {
        if (StringUtils.isBlank(customerName)) {
            logger.error("customerName is blank - input validation fail");
            return false;
        }

        String regx = "^[\\p{L} .'-]+$";
        Pattern pattern = Pattern.compile(regx, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(customerName);
        return matcher.find();
    }

    public boolean validatePhoneNumber(String phoneNumber) {
        if (StringUtils.isBlank(phoneNumber)) {
            logger.error("phoneNumber is blank - input validation fail");
            return false;
        }

        //validate phone numbers of format "1234567890"
        if (phoneNumber.matches("\\d{10}")) {
            return true;
        }
        //validate phone numbers of format "01234567890"
        if (phoneNumber.matches("0\\d{10}")) {
            return true;
        }
        //validate phone numbers of format "+1234567890"
        if (phoneNumber.matches("\\+\\d")) {
            return true;
        }
        //validating phone number with -, . or spaces
        else if (phoneNumber.matches("\\d{3}[-\\.\\s]\\d{3}[-\\.\\s]\\d{4}")) {
            return true;
        }
        //validating phone number with extension length from 3 to 5
        else if (phoneNumber.matches("\\d{3}-\\d{3}-\\d{4}\\s(x|(ext))\\d{3,5}")) {
            return true;
        }
        //validating phone number where area code is in braces ()
        else if (phoneNumber.matches("\\(\\d{3}\\)-\\d{3}-\\d{4}")) {
            return true;
        }
        //return false if nothing matches the input
        else {
            return false;
        }
    }
}
