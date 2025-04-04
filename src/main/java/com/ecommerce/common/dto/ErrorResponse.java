package com.ecommerce.common.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ErrorResponse {
    private LocalDateTime timestamp;
    private int status;
    private String error;
    private String message;
    private String path;
    private String oracleCode;
    private String oracleMessage;
    private String pgCode;
    private String pgMessage;

    public static ErrorResponse of(HttpStatus status, String message, String path) {
        return ErrorResponse.builder()
                .timestamp(LocalDateTime.now())
                .status(status.value())
                .error(status.getReasonPhrase())
                .message(message)
                .path(path)
                .oracleCode(null)
                .oracleMessage(null)
                .build();
    }

    public static ErrorResponse ofOracle(String oracleCode, String oracleMessage, String path) {
        return ErrorResponse.builder()
                .timestamp(LocalDateTime.now())
                .status(409)  // Conflict
                .error("Oracle Error")
                .message(String.format("%s: %s", oracleCode, oracleMessage))
                .path(path)
                .oracleCode(oracleCode)
                .oracleMessage(oracleMessage)
                .build();
    }

    public static ErrorResponse ofDatabase(String pgCode, String pgMessage, String path) {
        String fullPgCode = "PSQL-" + pgCode;
        return ErrorResponse.builder()
                .timestamp(LocalDateTime.now())
                .status(409)  // Conflict
                .error("Database Error")
                .message(String.format("%s: %s", fullPgCode, pgMessage))
                .path(path)
                .pgCode(fullPgCode)
                .pgMessage(pgMessage)
                .build();
    }
}
