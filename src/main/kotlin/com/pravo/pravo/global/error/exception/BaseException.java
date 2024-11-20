package com.pravo.pravo.global.error.exception;

import com.pravo.pravo.global.error.ErrorCode;
import com.pravo.pravo.global.error.ErrorCode;
import com.pravo.pravo.global.error.ErrorCode;
import com.pravo.pravo.global.error.ErrorCode;

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
