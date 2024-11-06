package com.pravo.pravo.global.error.exception;

import com.pravo.pravo.global.error.ErrorCode;
import com.pravo.pravo.global.error.ErrorCode;
import com.pravo.pravo.global.error.ErrorCode;
import com.pravo.pravo.global.error.ErrorCode;

public class BaseException extends RuntimeException {
    private final ErrorCode errorCode;

    public BaseException(ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
    }
}