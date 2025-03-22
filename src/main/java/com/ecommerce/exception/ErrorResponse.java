package com.ecommerce.exception;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import org.springframework.http.HttpStatus;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ErrorResponse {
    private int status;
    private String error;
    private String message;
    private String path;
    private String oracleCode;
    private String oracleMessage;
    private LocalDateTime timestamp;

    public static ErrorResponse ofOracle(String oracleCode, String oracleMessage, String path) {
        String fullOracleCode = "ORA-" + oracleCode;
        return ErrorResponse.builder()
                .status(409)
                .error("Oracle Error")
                .message(fullOracleCode + ": " + oracleMessage)
                .path(path)
                .oracleCode(fullOracleCode)
                .oracleMessage(oracleMessage)
                .timestamp(LocalDateTime.now())
                .build();
    }

    public static ErrorResponse of(HttpStatus status, String message, String path) {
        return ErrorResponse.builder()
                .status(status.value())
                .error(status.toString())
                .message(message)
                .path(path)
                .timestamp(LocalDateTime.now())
                .build();
    }
}
