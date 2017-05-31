package uk.co.serin.thule.core.aspects;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.lang.reflect.Proxy;

@Aspect
@Component
public class LogPublicMethodsInterceptor {
    @Around("(publicMethod() && pointcutDefinitionBasedOnAnnotationForType()) || pointcutDefinitionBasedOnAnnotationForMethod()")
    public Object log(ProceedingJoinPoint joinPoint) {
        return log(joinPoint.getTarget(), joinPoint.getSignature().getName(), joinPoint.getArgs(), new ProceedingJoinPointLoggerCallback(joinPoint));
    }

    private Object log(Object target, String methodName, Object[] args, PerformanceLoggerCallback performanceLoggerCallback) {
        Class loggingClass;
        if (Proxy.isProxyClass(target.getClass())) {
            loggingClass = target.getClass().getInterfaces()[0];
        } else {
            loggingClass = target.getClass();
        }
        Logger logger = LoggerFactory.getLogger(loggingClass);
        StringBuilder parameters = new StringBuilder();
        for (int index = 0; index < args.length; index++) {
            Object arg = args[index];
            parameters.append('[').append(arg).append((index == args.length - 1) ? ']' : "], ");
        }
        String params = (parameters.length() == 0) ? "with no parameters" : "with parameters " + parameters;
        logger.trace("Entering [{}] with method name of [{}] {}", target, methodName, params);

        long start = System.currentTimeMillis();
        Object returnValue = null;
        try {
            returnValue = performanceLoggerCallback.proceed();
        } finally {
            long elapsed = System.currentTimeMillis() - start;
            logger.trace("Exiting [{}] with method name of [{}] returning [{}] in {} ms", target, methodName, returnValue, elapsed);
        }

        return returnValue;
    }

    @Pointcut("@annotation(uk.co.serin.thule.core.aspects.LogPublicMethods)")
    public void pointcutDefinitionBasedOnAnnotationForMethod() {
        // Pointcut definition only
    }

    @Pointcut("within(@uk.co.serin.thule.core.aspects.LogPublicMethods *)")
    public void pointcutDefinitionBasedOnAnnotationForType() {
        // Pointcut definition only
    }

    @Pointcut("execution(public * *(..))")
    public void publicMethod() {
        // Pointcut definition only
    }

    @FunctionalInterface
    interface PerformanceLoggerCallback {
        Object proceed();
    }

    private static class ProceedingJoinPointLoggerCallback implements PerformanceLoggerCallback {
        private final ProceedingJoinPoint joinPoint;

        ProceedingJoinPointLoggerCallback(ProceedingJoinPoint joinPoint) {
            this.joinPoint = joinPoint;
        }

        @Override
        public Object proceed() {
            try {
                return joinPoint.proceed();
            } catch (RuntimeException e) {
                throw e;
            } catch (Throwable throwable) {
                throw new IllegalStateException(throwable);
            }
        }
    }
}