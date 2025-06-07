package ru.noir74.shop.misc.error.exceptions;

import org.springframework.http.HttpStatus;

public class ProductIsUsedException extends AppException {
    public ProductIsUsedException(String message, String details) {
        httpErrorStatus = HttpStatus.CONFLICT;
        this.cause = message;
        this.message = details;
    }
}
