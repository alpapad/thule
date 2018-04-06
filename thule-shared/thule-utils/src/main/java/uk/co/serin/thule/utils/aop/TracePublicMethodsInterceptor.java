package uk.co.serin.thule.utils.aop;

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
public class TracePublicMethodsInterceptor {
    @Pointcut("@annotation(uk.co.serin.thule.utils.aop.TracePublicMethods)")
    public void pointcutDefinitionBasedOnAnnotationForMethod() {
        // Pointcut definition only
    }

    @Pointcut("within(@uk.co.serin.thule.utils.aop.TracePublicMethods *)")
    public void pointcutDefinitionBasedOnAnnotationForType() {
        // Pointcut definition only
    }

    @Pointcut("execution(public * *(..))")
    public void publicMethod() {
        // Pointcut definition only
    }

    @Around("(publicMethod() && pointcutDefinitionBasedOnAnnotationForType()) || pointcutDefinitionBasedOnAnnotationForMethod()")
    public Object trace(ProceedingJoinPoint joinPoint) {
        return trace(joinPoint.getTarget(), joinPoint.getSignature().getName(), joinPoint.getArgs(), new ProceedingJoinPointTraceCallback(joinPoint));
    }

    private Object trace(Object target, String methodName, Object[] args, PerformanceTraceCallback performanceTraceCallback) {
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
            returnValue = performanceTraceCallback.proceed();
        } finally {
            long elapsed = System.currentTimeMillis() - start;
            logger.trace("Exiting [{}] with method name of [{}] returning [{}] in {} ms", target, methodName, returnValue, elapsed);
        }

        return returnValue;
    }

    @FunctionalInterface
    interface PerformanceTraceCallback {
        Object proceed();
    }

    private static class ProceedingJoinPointTraceCallback implements PerformanceTraceCallback {
        private final ProceedingJoinPoint joinPoint;

        ProceedingJoinPointTraceCallback(ProceedingJoinPoint joinPoint) {
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