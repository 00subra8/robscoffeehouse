package com.ig.eval.service;

import com.ig.eval.dao.CoffeeHouseDAO;
import com.ig.eval.exception.CoffeeHouseInputException;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class InputValidatorService {

    public static final String DECIMAL_SEPERATOR_REGEX = "\\.";
    public static final String DEFAULT_BLANK_STR = "";
    private Logger logger = LoggerFactory.getLogger(InputValidatorService.class);

    @Autowired
    private CoffeeHouseDAO coffeeHouseDAO;


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

        if (phoneNumber.matches("\\d{10}")) {
            return true;
        }
        if (phoneNumber.matches("0\\d{10}")) {
            return true;
        }
        if (phoneNumber.matches("^[+]\\d{12,13}")) {
            return true;
        } else if (phoneNumber.matches("\\d{3}[-\\.\\s]\\d{3}[-\\.\\s]\\d{4}")) {
            return true;
        } else if (phoneNumber.matches("\\d{3}-\\d{3}-\\d{4}\\s(x|(ext))\\d{3,5}")) {
            return true;
        } else if (phoneNumber.matches("\\(\\d{3}\\)-\\d{3}-\\d{4}")) {
            return true;
        } else {
            return false;
        }
    }

    public boolean isPhoneNumberUnique(String phoneNumber) {
        List<String> allPhoneNumbers = coffeeHouseDAO.getAllPhoneNumbers();

        if (CollectionUtils.isEmpty(allPhoneNumbers)) {
            return true;
        }

        Optional<String> matchedPhoneNumber = getMatch(phoneNumber, allPhoneNumbers);

        return !matchedPhoneNumber.isPresent();
    }

    public boolean isVarietyUnique(String coffeeVarietyName) {
        List<String> allVarietyNames = coffeeHouseDAO.getAllVarietyNames();

        if (CollectionUtils.isEmpty(allVarietyNames)) {
            return true;
        }

        Optional<String> matchedVarietyName = getMatch(coffeeVarietyName, allVarietyNames);

        return !matchedVarietyName.isPresent();
    }

    private Optional<String> getMatch(String matchString, List<String> allValues) {
        return CollectionUtils.emptyIfNull(allValues).stream()
                .filter(StringUtils::isNotBlank)
                .filter(currentPhoneNumber -> StringUtils.equalsIgnoreCase(StringUtils.trim(matchString),
                        StringUtils.trim(currentPhoneNumber)))
                .findFirst();
    }

    public boolean isAvailabilityValid(String availableQuantity) {
        try {
            int intAvailableQuantity = Integer.parseInt(availableQuantity);
            if (intAvailableQuantity < 0 || intAvailableQuantity > 300) {
                return false;
            }
        } catch (NumberFormatException nfe) {
            String availableQuantityInvalidMessage = "Available quantity not a number";
            logger.error(availableQuantityInvalidMessage);
            throw new CoffeeHouseInputException(availableQuantityInvalidMessage);
        }

        return true;
    }

    public boolean isVarietyPresent(String coffeeVarietyName) {
        List<String> allVarietyNames = coffeeHouseDAO.getAllVarietyNames();

        Optional<String> matchedVariety = getMatch(coffeeVarietyName, allVarietyNames);

        return matchedVariety.isPresent();
    }

    public boolean isItemAvailable(String coffeeVarietyName, String quantity) {
        List<String> allVarietyNames = coffeeHouseDAO.getAllVarietyNames();

        Optional<String> matchedVariety = getMatch(coffeeVarietyName, allVarietyNames);
        if (matchedVariety.isPresent()) {
            int availableQuantity = coffeeHouseDAO.getAvailableQuantity(matchedVariety.get());
            try {
                return availableQuantity >= Integer.valueOf(quantity);
            } catch (NumberFormatException nfe) {
                String availableQuantityInvalidMessage = "quantity not a number";
                logger.error(availableQuantityInvalidMessage);
                throw new CoffeeHouseInputException(availableQuantityInvalidMessage);
            }
        }
        return false;
    }

    public boolean isPriceValid(String price) {
        int decimalDigitsLength = getDecimalDigits(price);
        if (decimalDigitsLength > 2) {
            return false;
        }
        try {
            double doublePrice = Double.parseDouble(price);
            if (doublePrice < 0) {
                return false;
            }
        } catch (NumberFormatException | NullPointerException nfe) {
            String priceInvalidMessage = "Price not a number";
            logger.error(priceInvalidMessage);
            throw new CoffeeHouseInputException(priceInvalidMessage);
        }

        return true;
    }

    private int getDecimalDigits(String price) {
        if (StringUtils.isNotBlank(price)) {
            String[] splitPrice = price.split(DECIMAL_SEPERATOR_REGEX);
            if (splitPrice.length > 1) {
                return StringUtils.defaultIfBlank(splitPrice[1], DEFAULT_BLANK_STR).length();
            }
        }
        return 0;
    }

    public boolean isCustomerValid(String customerPhoneNumber) {
        List<String> allPhoneNumbers = coffeeHouseDAO.getAllPhoneNumbers();

        if (CollectionUtils.isEmpty(allPhoneNumbers)) {
            return false;
        }

        Optional<String> matchedPhoneNumber = getMatch(customerPhoneNumber, allPhoneNumbers);
        return matchedPhoneNumber.isPresent();
    }

}
