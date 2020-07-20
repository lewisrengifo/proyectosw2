package com.example.demo.Controllers;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class ControllerError {
    @ExceptionHandler(Exception.class)
    public String exceptionHandler(){
        return "login/error500";
    }
}
