package uk.co.serin.thule.utils.trace;

import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.ILoggerFactory;
import org.slf4j.LoggerFactory;

@Aspect
public class LogExceptionInterceptor {
    private static ILoggerFactory loggerFactory = LoggerFactory.getILoggerFactory();

    @AfterThrowing(pointcut = "(pointcutDefinitionBasedOnAnnotationForType()) || pointcutDefinitionBasedOnAnnotationForMethod()", throwing = "throwable")
    public void log(Throwable throwable) {
        var loggerName = (throwable.getStackTrace() != null && throwable.getStackTrace().length > 0) ?
                throwable.getStackTrace()[0].getClassName() : LogExceptionInterceptor.class.getName();
        var logger = loggerFactory.getLogger(loggerName);
        logger.error(throwable.getMessage(), throwable);
    }

    @Pointcut("@annotation(uk.co.serin.thule.utils.trace.LogException)")
    public void pointcutDefinitionBasedOnAnnotationForMethod() {
        // Pointcut definition only
    }

    @Pointcut("within(@uk.co.serin.thule.utils.trace.LogException *)")
    public void pointcutDefinitionBasedOnAnnotationForType() {
        // Pointcut definition only
    }
}
