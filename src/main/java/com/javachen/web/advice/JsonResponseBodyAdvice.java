package com.javachen.web.advice;

import com.javachen.common.utils.JsonUtils;
import com.javachen.web.CommonResponse;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.lang.Nullable;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

import java.lang.reflect.Method;
import java.util.Map;
import java.util.WeakHashMap;

@RestControllerAdvice("com.wesine.ads.web")
public class JsonResponseBodyAdvice implements ResponseBodyAdvice<Object> {
    private final Map<Method, Boolean> supportsCache = new WeakHashMap<>();

    @Override
    public boolean supports(MethodParameter returnType, Class<? extends HttpMessageConverter<?>> converterType) {
        if (supportsCache.containsKey(returnType.getMethod())) {
            return supportsCache.get(returnType.getMethod());
        }
        boolean isSupport = getIsSupport(returnType);
        supportsCache.put(returnType.getMethod(), isSupport);
        return isSupport;
    }

    private boolean getIsSupport(MethodParameter returnType) {
        Class<?> declaringClass = returnType.getMember().getDeclaringClass();
        IgnoreReponseAdvice classIgnore = declaringClass.getAnnotation(IgnoreReponseAdvice.class);
        IgnoreReponseAdvice methodIgnore = returnType.getMethod().getAnnotation(IgnoreReponseAdvice.class);
        if (classIgnore != null || methodIgnore != null) {
            return false;
        }
        return true;
    }

    @Override
    public Object beforeBodyWrite(@Nullable Object body, MethodParameter returnType, MediaType selectedContentType,
                                  Class<? extends HttpMessageConverter<?>> selectedConverterType,
                                  ServerHttpRequest request, ServerHttpResponse response) {
        // 判断为null构建ResponseData对象进行返回
        if (body == null) {
            return CommonResponse.success();
        }
        // 判断是ResponseData子类或其本身就返回Object o本身，因为有可能是接口返回时创建了ResponseData,这里避免再次封装
        if (body instanceof CommonResponse) {
            return (CommonResponse<Object>) body;
        }
        // String特殊处理，否则会抛异常
        if (body instanceof String) {
            return JsonUtils.toJson(CommonResponse.success(body));
        }
        return CommonResponse.success(body);
    }
}
