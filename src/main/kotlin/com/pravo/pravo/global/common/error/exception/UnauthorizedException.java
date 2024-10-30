package com.pravo.pravo.global.common.error.exception;

import com.pravo.pravo.global.common.error.ErrorCode;

public class UnauthorizedException extends BaseException {
    public UnauthorizedException(ErrorCode errorCode) {
        super(errorCode);
    }

    public UnauthorizedException(ErrorCode errorCode, String message) {
        super(errorCode, message);
    }
}