package com.javachen.web;

import com.javachen.common.exception.ErrorCode;
import com.javachen.common.exception.ErrorCodeAware;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CommonResponse<T> implements java.io.Serializable {
    private String code;
    private String message;
    private T data;

    private CommonResponse(ErrorCodeAware errorCodeAware, T data) {
        this.code = errorCodeAware.getCode();
        this.message = errorCodeAware.getMessage();
        this.data = data;
    }

    private CommonResponse(ErrorCodeAware errorCodeAware) {
        this(errorCodeAware, null);
    }

    public static CommonResponse success() {
        return new CommonResponse(ErrorCode.SUCCESS, null);
    }

    public static CommonResponse success(Object object) {
        return new CommonResponse(ErrorCode.SUCCESS, object);
    }

    public static CommonResponse error(ErrorCodeAware errorCodeAware) {
        return new CommonResponse(errorCodeAware);
    }

    public static CommonResponse error(ErrorCodeAware errorCodeAware, String message) {
        return new CommonResponse(errorCodeAware.getCode(), message, null);
    }
}
