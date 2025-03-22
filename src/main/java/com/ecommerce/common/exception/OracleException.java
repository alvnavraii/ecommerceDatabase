package com.ecommerce.common.exception;

import lombok.Getter;

@Getter
public class OracleException extends RuntimeException {
    private final String oracleCode;
    private final String oracleMessage;

    public OracleException(String code, String message) {
        super(code + ": " + message);
        this.oracleCode = code;
        this.oracleMessage = message;
    }
}
