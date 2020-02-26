package com.javachen.common.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

import javax.servlet.http.HttpServletResponse;

@Getter
@AllArgsConstructor
public enum ErrorCode implements ErrorCodeAware {
    SUCCESS(String.valueOf(HttpServletResponse.SC_OK), "success"),

    FAILURE(String.valueOf(HttpServletResponse.SC_BAD_REQUEST), "failure"),

    UN_AUTHORIZED(String.valueOf(HttpServletResponse.SC_UNAUTHORIZED), "Request Unauthorized"),

    NOT_FOUND(String.valueOf(HttpServletResponse.SC_NOT_FOUND), "404 Not Found"),

    MSG_NOT_READABLE(String.valueOf(HttpServletResponse.SC_BAD_REQUEST), "Message Can't be Read"),

    METHOD_NOT_SUPPORTED(String.valueOf(HttpServletResponse.SC_METHOD_NOT_ALLOWED), "Method Not Supported"),

    MEDIA_TYPE_NOT_SUPPORTED(String.valueOf(HttpServletResponse.SC_UNSUPPORTED_MEDIA_TYPE), "Media Type Not Supported"),

    REQ_REJECT(String.valueOf(HttpServletResponse.SC_FORBIDDEN), "Request Rejected"),

    INTERNAL_SERVER_ERROR(String.valueOf(HttpServletResponse.SC_INTERNAL_SERVER_ERROR), "Internal Server Error"),

    PARAM_MISS(String.valueOf(HttpServletResponse.SC_BAD_REQUEST), "Missing Required Parameter"),

    PARAM_TYPE_ERROR(String.valueOf(HttpServletResponse.SC_BAD_REQUEST), "Parameter Type Mismatch"),

    PARAM_BIND_ERROR(String.valueOf(HttpServletResponse.SC_BAD_REQUEST), "Parameter Binding Error"),

    PARAM_VALID_ERROR(String.valueOf(HttpServletResponse.SC_BAD_REQUEST), "Parameter Validation Error"),
    ;

    String code;

    String message;

    @Override
    public ErrorCodeAware setMessage(String message) {
        this.message = message;
        return this;
    }
}
