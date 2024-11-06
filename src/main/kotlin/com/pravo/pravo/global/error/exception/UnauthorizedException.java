package com.pravo.pravo.global.error.exception;

import com.pravo.pravo.global.error.ErrorCode;

public class UnauthorizedException extends BaseException {
    public UnauthorizedException(ErrorCode errorCode) {
        super(errorCode);
    }
}