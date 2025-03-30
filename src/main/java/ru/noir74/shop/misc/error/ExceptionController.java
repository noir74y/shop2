package ru.noir74.shop.misc.error;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import ru.noir74.shop.misc.error.exceptions.AppException;
import ru.noir74.shop.misc.error.exceptions.NotFoundException;
import ru.noir74.shop.misc.error.exceptions.ProductIsUsedException;

@Slf4j
//@ControllerAdvice
public class ExceptionController {
    @ExceptionHandler(Exception.class)
    public String exceptionController(Exception exception) {
        AppException appException;

        log.error("{}", exception.getMessage());

        switch (exception) {
            case ProductIsUsedException productIsUsedException -> {
                return "error-is-used.html";
            }
            case NotFoundException notFoundException -> {
                return "error-not-found.html";
            }
            default -> {
                return "error.html";
            }
        }
    }
}
