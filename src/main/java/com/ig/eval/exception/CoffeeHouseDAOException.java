package com.ig.eval.exception;

import org.springframework.dao.DataAccessException;

public class CoffeeHouseDAOException extends DataAccessException {
    public CoffeeHouseDAOException(String messsage) {
        super(messsage);
    }
}
