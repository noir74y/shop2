package ru.noir74.shop.controllers;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.lang.Nullable;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

@Slf4j
public class LoggingInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(@NonNull HttpServletRequest request,
                             @Nullable HttpServletResponse response,
                             @Nullable Object handler) throws Exception {
        log.info("{} {}", request.getMethod(),request.getServletPath());

        if (handler instanceof HandlerMethod handlerMethod) {
            String controllerName = handlerMethod.getBean().getClass().getSimpleName();
            String methodName = handlerMethod.getMethod().getName();
            log.info("{}.{}()", controllerName,methodName);
        }
        return true;
    }
}
