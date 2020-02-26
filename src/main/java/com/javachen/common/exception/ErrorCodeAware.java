package com.javachen.common.exception;

public interface ErrorCodeAware {
    public String getCode();

    public String getMessage();

    public ErrorCodeAware setMessage(String message);
}
