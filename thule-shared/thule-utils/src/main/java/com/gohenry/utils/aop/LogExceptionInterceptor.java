package com.gohenry.utils.aop;

import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.ILoggerFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class LogExceptionInterceptor {
    private static ILoggerFactory loggerFactory = LoggerFactory.getILoggerFactory();

    @AfterThrowing(pointcut = "(pointcutDefinitionBasedOnAnnotationForType()) || pointcutDefinitionBasedOnAnnotationForMethod()", throwing = "throwable")
    public void log(Throwable throwable) {
        String loggerName = (throwable.getStackTrace() != null && throwable.getStackTrace().length > 0) ?
                throwable.getStackTrace()[0].getClassName() : LogExceptionInterceptor.class.getName();
        Logger logger = loggerFactory.getLogger(loggerName);
        logger.error(throwable.getMessage(), throwable);
    }

    @Pointcut("@annotation(com.gohenry.utils.aop.LogException)")
    public void pointcutDefinitionBasedOnAnnotationForMethod() {
        // Pointcut definition only.
    }

    @Pointcut("within(@com.gohenry.utils.aop.LogException *)")
    public void pointcutDefinitionBasedOnAnnotationForType() {
        // Pointcut definition only.
    }
}
