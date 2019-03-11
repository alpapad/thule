package uk.co.serin.thule.utils.service.trace;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.lang.reflect.Proxy;

@Aspect
@Component
public class TracePublicMethodsInterceptor {
    @Pointcut("@annotation(uk.co.serin.thule.utils.service.trace.TracePublicMethods)")
    public void pointcutDefinitionBasedOnAnnotationForMethod() {
        // Pointcut definition only
    }

    @Pointcut("within(@uk.co.serin.thule.utils.service.trace.TracePublicMethods *)")
    public void pointcutDefinitionBasedOnAnnotationForType() {
        // Pointcut definition only
    }

    @Pointcut("execution(public * *(..))")
    public void publicMethod() {
        // Pointcut definition only
    }

    @Around("(publicMethod() && pointcutDefinitionBasedOnAnnotationForType()) || pointcutDefinitionBasedOnAnnotationForMethod()")
    public Object trace(ProceedingJoinPoint joinPoint) {
        return trace(joinPoint.getTarget(), joinPoint.getSignature(), joinPoint.getArgs(), new ProceedingJoinPointTraceCallback(joinPoint));
    }

    private Object trace(Object target, Signature signature, Object[] args, PerformanceTraceCallback performanceTraceCallback) {
        var methodName = signature.getName();
        var returnType = ((MethodSignature) signature).getReturnType();

        Class loggingClass;
        if (Proxy.isProxyClass(target.getClass())) {
            loggingClass = target.getClass().getInterfaces()[0];
        } else {
            loggingClass = target.getClass();
        }
        var logger = LoggerFactory.getLogger(loggingClass);
        var parameters = new StringBuilder();
        for (var index = 0; index < args.length; index++) {
            var arg = args[index];
            parameters.append('[').append(arg).append((index == args.length - 1) ? ']' : "], ");
        }
        var params = (parameters.length() == 0) ? "using no parameters" : "using parameters " + parameters;
        logger.trace("Entering [{}] with method name of [{}] {}", target, methodName, params);

        var start = System.currentTimeMillis();
        Object returnValue = null;
        try {
            returnValue = performanceTraceCallback.proceed();
        } finally {
            var elapsed = System.currentTimeMillis() - start;
            var returnMessage = (returnType == Void.TYPE) ? "void" : returnValue;
            logger.trace("Exiting [{}] with method name of [{}] returning [{}] in {} ms", target, methodName, returnMessage, elapsed);
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