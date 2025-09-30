package com.wb.hotel_reservations_api.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.CONFLICT)
public class BusinessRuleException extends RuntimeException{

    public BusinessRuleException(String message) {
        super(message);
    }
}
