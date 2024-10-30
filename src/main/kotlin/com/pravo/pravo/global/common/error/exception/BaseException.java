package com.pravo.pravo.global.common.error.exception;

import com.pravo.pravo.global.common.error.ErrorCode;

public class BaseException extends RuntimeException {
    private final ErrorCode errorCode;

    public BaseException(ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
    }
}
