package com.javachen.common.aspect;

import com.google.common.util.concurrent.RateLimiter;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;

@Aspect
@Configuration
@Slf4j
@Scope
public class RequestLimitAspect {

    private static long limit;

    private static RateLimiter rateLimiter;

    @Value("${server.request.limit: 20}")
    public void setLimit(long limit) {
        RequestLimitAspect.limit = limit;
        rateLimiter = RateLimiter.create(limit);

    }

    //Controller层切点  限流
    @Pointcut("@annotation(com.javachen.common.annotation.RequestLimit)")
    public void requestAspect() {

    }

    @Around("requestAspect()")
    public Object around(ProceedingJoinPoint joinPoint) throws Throwable {
        Boolean flag = rateLimiter.tryAcquire();
        Object obj = null;
        if (flag) {
            obj = joinPoint.proceed();
        } else {
            log.info("哎哟喂，业务太火爆，稍等一会儿呗");
            throw new Exception("哎哟喂，业务太火爆，稍等一会儿呗");
        }
        return obj;
    }
}
