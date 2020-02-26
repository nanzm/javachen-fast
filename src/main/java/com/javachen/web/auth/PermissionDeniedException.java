package com.javachen.web.auth;

import com.javachen.common.exception.ErrorCode;
import lombok.Getter;

public class PermissionDeniedException extends RuntimeException {
    @Getter
    private final ErrorCode errorCode;

    public PermissionDeniedException(String message) {
        super(message);
        this.errorCode = ErrorCode.UN_AUTHORIZED;
    }

    public PermissionDeniedException(ErrorCode resultCode) {
        super(resultCode.getMessage());
        this.errorCode = resultCode;
    }

    public PermissionDeniedException(ErrorCode resultCode, Throwable cause) {
        super(cause);
        this.errorCode = resultCode;
    }

    @Override
    public Throwable fillInStackTrace() {
        return this;
    }
}
