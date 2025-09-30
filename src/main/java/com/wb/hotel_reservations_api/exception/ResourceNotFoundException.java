package com.wb.hotel_reservations_api.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class ResourceNotFoundException extends RuntimeException{

    public ResourceNotFoundException(String resourceName, Long id) {
        super(String.format("%s não encontrado(a) com ID: %d", resourceName, id));
    }
}
