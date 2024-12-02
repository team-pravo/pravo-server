package com.pravo.pravo.global.error.exception;

import com.pravo.pravo.global.error.ErrorCode;

public class BadRequestException extends BaseException {
    public BadRequestException() {
        super(ErrorCode.BAD_REQUEST);
    }

    public BadRequestException(String message) {
        super(ErrorCode.BAD_REQUEST, message);
    }

}
