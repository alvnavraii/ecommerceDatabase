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
    private String pgCode;
    private String pgMessage;
    private LocalDateTime timestamp;

    public static ErrorResponse ofDatabase(String pgCode, String pgMessage, String path) {
        String fullPgCode = "PSQL-" + pgCode;
        return ErrorResponse.builder()
                .status(409)
                .error("Database Error")
                .message(fullPgCode + ": " + pgMessage)
                .path(path)
                .pgCode(fullPgCode)
                .pgMessage(pgMessage)
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
