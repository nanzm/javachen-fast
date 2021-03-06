package com.javachen.common.exception;

import lombok.Getter;

/**
 * //包装器业务异常类实现
 */
@Getter
public class BusinessException extends RuntimeException implements ErrorCodeAware {

    private ErrorCodeAware errorCodeAware;

    public BusinessException(String message) {
        super(message);
        this.errorCodeAware = ErrorCode.FAILURE;
        this.errorCodeAware.setMessage(message);
    }

    //直接接收EmBusinessError的传参用于构造业务异常
    public BusinessException(ErrorCodeAware errorCodeAware) {
        super(errorCodeAware.getMessage());
        this.errorCodeAware = errorCodeAware;
    }

    //接收自定义errorMsg的方式构造业务异常
    public BusinessException(ErrorCodeAware errorCodeAware, String message) {
        super(message);
        this.errorCodeAware = errorCodeAware;
        this.errorCodeAware.setMessage(message);
    }

    public BusinessException(ErrorCodeAware errorCodeAware, Throwable cause) {
        super(cause);
        this.errorCodeAware = errorCodeAware;
    }

    public BusinessException(String message, Throwable cause) {
        super(message, cause);
        this.errorCodeAware = ErrorCode.FAILURE;
        this.errorCodeAware.setMessage(message);
    }

    @Override
    public String getCode() {
        return this.errorCodeAware.getCode();
    }

    @Override
    public String getMessage() {
        return this.errorCodeAware.getMessage();
    }

    @Override
    public ErrorCodeAware setMessage(String message) {
        this.errorCodeAware.setMessage(message);
        return this;
    }

    public ErrorCodeAware getErrorCodeAware() {
        return errorCodeAware;
    }
}
