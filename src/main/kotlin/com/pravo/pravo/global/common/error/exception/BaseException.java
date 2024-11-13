package com.pravo.pravo.global.common.error.exception;

import com.pravo.pravo.global.common.error.ErrorCode;

public class BaseException extends RuntimeException {
    public final ErrorCode errorCode;

    public BaseException(ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
    }

    public BaseException(ErrorCode errorCode, String message) {
        super(message);
        this.errorCode = errorCode;
    }

    @Override
    public String getMessage() {
        return super.getMessage();
    }
}
