package ru.noir74.shop.misc.exceptions;

import org.springframework.http.HttpStatus;

public class ProductIsUsedException extends AppException {
    public ProductIsUsedException(String message, String details) {
        httpErrorStatus = HttpStatus.CONFLICT;
        this.cause = message;
        this.message = details;
    }
}
