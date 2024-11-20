package com.pravo.pravo.global.error.exception;

import com.pravo.pravo.global.error.ErrorCode;

public class NotFoundException extends BaseException {
    public NotFoundException(ErrorCode errorCode) {
        super(errorCode);
    }

    public NotFoundException(ErrorCode errorCode, String message) {
        super(errorCode, message);
    }

}
