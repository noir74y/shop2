package ru.noir74.shop.misc.error.exceptions;

import lombok.Getter;
import org.springframework.http.HttpStatus;
import ru.noir74.shop.misc.error.ErrorMessage;


public class AppException extends RuntimeException {
    @Getter
    protected HttpStatus httpErrorStatus;
    protected String cause;
    protected String message;

    public ErrorMessage prepareErrorMessage() {
        return new ErrorMessage(cause, message);
    }

}
