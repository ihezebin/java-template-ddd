package com.hezebin.template.exception;

import com.hezebin.template.application.dto.ResponseBodyCode;

public class ErrorException extends RuntimeException {
    private final ResponseBodyCode code;

    public ErrorException(ResponseBodyCode code, String message) {
        super(message);
        this.code = code;
    }

    public ErrorException(ResponseBodyCode code, Exception e) {
        super(e.getMessage());
        this.code = code;
    }

    public ResponseBodyCode getCode() {
        return code;
    }
}