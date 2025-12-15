package com.redcare.git.demo.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;

public record ErrorResponse(
        HttpStatus httpStatus,
        @JsonFormat(pattern = "yyyyMMdd hh:mm:ss") LocalDateTime timeStamp,
        String message
) {
}
