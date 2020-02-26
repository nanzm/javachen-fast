package com.javachen.common.exception;

import com.google.common.collect.ImmutableMap;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindException;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.NoHandlerFoundException;

import javax.validation.ConstraintViolationException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.UndeclaredThrowableException;

public class ExceptionHelper {
    private static final ImmutableMap<Class<? extends Throwable>, ErrorCode> MAP = ImmutableMap.<Class<? extends Throwable>, ErrorCode>builder()
            .put(HttpMediaTypeNotSupportedException.class, ErrorCode.MEDIA_TYPE_NOT_SUPPORTED)
            .put(HttpRequestMethodNotSupportedException.class, ErrorCode.METHOD_NOT_SUPPORTED)
            .put(MissingServletRequestParameterException.class, ErrorCode.PARAM_MISS)
            .put(MethodArgumentTypeMismatchException.class, ErrorCode.PARAM_TYPE_ERROR)
            .put(MethodArgumentNotValidException.class, ErrorCode.PARAM_VALID_ERROR)
            .put(BindException.class, ErrorCode.PARAM_BIND_ERROR)
            .put(ConstraintViolationException.class, ErrorCode.PARAM_VALID_ERROR)
            .put(NoHandlerFoundException.class, ErrorCode.NOT_FOUND)
            .put(HttpMessageNotReadableException.class, ErrorCode.MSG_NOT_READABLE)
            .build();

    public static ErrorCode getErrorCode(Throwable throwable) {
        ErrorCode errorCode = ErrorCode.INTERNAL_SERVER_ERROR;
        if (throwable == null) {
            return errorCode;
        }
        if (throwable instanceof BusinessException) {
            return (ErrorCode) ((BusinessException) throwable).getErrorCodeAware();
        } else {
            errorCode = MAP.get(throwable.getClass());
        }
        //没有定义
        if (errorCode == null) {
            //根异常
            Throwable rootCause = ExceptionUtils.getRootCause(throwable);
            if (!rootCause.equals(throwable) && rootCause != null) {
                errorCode = MAP.get(rootCause.getClass());
            }
        }

        return errorCode == null ? ErrorCode.INTERNAL_SERVER_ERROR : errorCode;
    }

    public static void cast(ErrorCodeAware errorCodeAware) {
        throw new BusinessException(errorCodeAware);
    }

    public static void cast(ErrorCodeAware errorCodeAware, String message) {
        throw new BusinessException(errorCodeAware, message);
    }

    private static final Log LOG = LogFactory.getLog(ExceptionHelper.class);

    public static <G extends Throwable, J extends RuntimeException> RuntimeException refineException(Class<G> refineType, Class<J> wrapType, String message, Throwable e) {
        if (refineType.isAssignableFrom(e.getClass())) {
            return wrapException(e, wrapType, message);
        }
        if (e.getCause() != null) {
            return refineException(refineType, wrapType, message, e.getCause());
        }
        if (e instanceof UndeclaredThrowableException) {
            return refineException(refineType, wrapType, message, ((UndeclaredThrowableException) e).getUndeclaredThrowable());
        }
        if (e instanceof InvocationTargetException) {
            return refineException(refineType, wrapType, message, ((InvocationTargetException) e).getTargetException());
        }
        return wrapException(e, wrapType, message);
    }

    public static <G extends Throwable, J extends RuntimeException> RuntimeException refineException(Class<G> refineType, Class<J> wrapType, Throwable e) {
        return refineException(refineType, wrapType, null, e);
    }

    public static <G extends Throwable, J extends RuntimeException> RuntimeException refineException(Class<J> wrapType, Throwable e) {
        return refineException(RuntimeException.class, wrapType, null, e);
    }

    public static <G extends Throwable, J extends RuntimeException> RuntimeException refineException(Throwable e) {
        return refineException(RuntimeException.class, RuntimeException.class, null, e);
    }

    public static <G extends Throwable, J extends RuntimeException> void processException(Class<G> refineType, Class<J> wrapType, String message, Throwable e) throws G {
        if (refineType.isAssignableFrom(e.getClass())) {
            throw (G) e;
        }
        if (e.getCause() != null) {
            processException(refineType, wrapType, message, e.getCause());
        }
        if (e instanceof UndeclaredThrowableException) {
            processException(refineType, wrapType, message, ((UndeclaredThrowableException) e).getUndeclaredThrowable());
        }
        if (e instanceof InvocationTargetException) {
            processException(refineType, wrapType, message, ((InvocationTargetException) e).getTargetException());
        }
        throw wrapException(e, wrapType, message);
    }

    public static <G extends Throwable, J extends RuntimeException> void processException(Class<G> refineType, Class<J> wrapType, Throwable e) throws G {
        processException(refineType, wrapType, null, e);
    }

    public static <G extends Throwable, J extends RuntimeException> void processException(Class<J> wrapType, Throwable e) throws G {
        processException(RuntimeException.class, wrapType, null, e);
    }

    public static <G extends Throwable, J extends RuntimeException> void processException(Throwable e) throws G {
        processException(RuntimeException.class, RuntimeException.class, null, e);
    }

    private static <J extends RuntimeException> RuntimeException wrapException(Throwable e, Class<J> wrapType, String message) {
        if (e instanceof RuntimeException) {
            return (RuntimeException) e;
        }
        try {
            if (StringUtils.isEmpty(message)) {
                return wrapType.getConstructor(Throwable.class).newInstance(e);
            } else {
                return wrapType.getConstructor(String.class, Throwable.class).newInstance(message, e);
            }
        } catch (Exception e1) {
            LOG.error("Could not wrap exception", e1);
            throw new RuntimeException(e);
        }
    }
}
