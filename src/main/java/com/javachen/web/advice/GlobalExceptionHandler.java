package com.javachen.web.advice;

import com.javachen.common.exception.ErrorCode;
import com.javachen.common.exception.ExceptionHelper;
import com.javachen.web.CommonResponse;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.MethodParameter;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.servlet.http.HttpServletRequest;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 微服务，json全部返回200
 *
 * @author june
 * @createTime 2019-06-24 22:02
 * @see
 * @since
 */
@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {
    @Autowired
    private ConstraintViolationMapper constraintViolationMapper;

    private void handleException(String errMsg, Throwable e) {
        log.error(errMsg, e);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public CommonResponse handleMethodArgumentNotValidException(HttpServletRequest request, MethodArgumentNotValidException ex) {
        handleException("Method Argument Not Valid", ex);

        Set<ConstraintViolation<?>> violations;
        try {
            violations = ex.getBindingResult()
                    .getAllErrors()
                    .stream()
                    .map(e -> (ConstraintViolation<?>) e.unwrap(ConstraintViolation.class))
                    .collect(Collectors.toSet());
        } catch (IllegalArgumentException e) {
            // no wrapped constraint violation
            return handleBadRequestException(e);
        }

        return handleViolations(violations, ex.getParameter(), ex);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public CommonResponse handleConstraintViolationException(ConstraintViolationException ex) {
        handleException("ConstraintViolation Not Valid", ex);
        return handleViolations(ex.getConstraintViolations(), null, ex);
    }

    private CommonResponse handleViolations(
            Set<ConstraintViolation<?>> violations,
            MethodParameter parameter,
            Exception ex) {
        if (violations != null && !violations.isEmpty()) {
            return CommonResponse
                    .builder()
                    .code(ErrorCode.PARAM_BIND_ERROR.getCode())
                    .message(ErrorCode.PARAM_BIND_ERROR.getMessage())
                    .data(violations.stream()
                            .map(v -> constraintViolationMapper.mapToBadRequestDetails(v, parameter))
                            .collect(Collectors.toList()))
                    .build();
        }
        return handleBadRequestException(ex);
    }

    @ExceptionHandler(Throwable.class)
    public CommonResponse handleBadRequestException(Throwable e) {
        handleException("Internal Server Error", e);

        ErrorCode errorCode = ExceptionHelper.getErrorCode(e);
        String message = ExceptionUtils.getRootCause(e).getMessage(); //最初的异常原因
        return CommonResponse
                .builder()
                .code(errorCode.getCode())
                .message(message)
                .build();
    }
}
