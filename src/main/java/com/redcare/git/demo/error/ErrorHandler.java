package com.redcare.git.demo.error;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
@Slf4j
public class ErrorHandler {

    @ExceptionHandler
    public ResponseEntity<String> handleException(Exception e) {
        log.error("An error ocurred", e);
        return ResponseEntity.status(500).body("Something went wrong");
    }
}
