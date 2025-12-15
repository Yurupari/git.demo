package com.redcare.git.demo.error;

import com.redcare.git.demo.dto.ErrorResponse;
import com.redcare.git.demo.exception.GitDataParseException;
import com.redcare.git.demo.exception.GitHubServiceException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.LocalDateTime;

@ControllerAdvice
public class ErrorHandler {

    @ExceptionHandler
    public ResponseEntity<ErrorResponse> handleGitHubServiceException(GitHubServiceException e) {
        ErrorResponse errorResponse = buildErrorResponse(HttpStatus.SERVICE_UNAVAILABLE, e.getMessage());
        return new ResponseEntity<>(errorResponse, HttpStatus.SERVICE_UNAVAILABLE);
    }

    @ExceptionHandler
    public ResponseEntity<ErrorResponse> handleGitDataParseException(GitDataParseException e) {
        ErrorResponse errorResponse = buildErrorResponse(HttpStatus.BAD_GATEWAY, e.getMessage());
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_GATEWAY);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ErrorResponse> handleHttpMessageNotReadableException(HttpMessageNotReadableException e) {
        ErrorResponse errorResponse = buildErrorResponse(HttpStatus.BAD_REQUEST, e.getMessage());
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler
    public ResponseEntity<ErrorResponse> handleException(Exception e) {
        ErrorResponse errorResponse = buildErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    private ErrorResponse buildErrorResponse(HttpStatus httpStatus, String errorMessage) {
        return new ErrorResponse(httpStatus, LocalDateTime.now(), errorMessage);
    }
}
