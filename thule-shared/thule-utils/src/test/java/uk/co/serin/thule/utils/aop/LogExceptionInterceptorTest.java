package uk.co.serin.thule.utils.aop;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.slf4j.ILoggerFactory;
import org.slf4j.Logger;
import org.springframework.test.util.ReflectionTestUtils;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class LogExceptionInterceptorTest {
    @Mock
    private ILoggerFactory iLoggerFactory;
    private LogExceptionInterceptor logExceptionInterceptor = new LogExceptionInterceptor();
    @Mock
    private Logger logger;
    @Mock
    private Throwable throwable;

    @Test
    public void empty_pointcuts_do_not_throw_an_exception() {
        // Given

        // When
        logExceptionInterceptor.pointcutDefinitionBasedOnAnnotationForMethod();
        logExceptionInterceptor.pointcutDefinitionBasedOnAnnotationForType();

        // Then (no exception is a success)
    }

    @Test
    public void log_throwable_issues_error_message_using_classname_of_the_LogExceptionInterceptor_when_the_stacktrace_is_null() {
        // Given
        String message = "Test throwable message";
        ReflectionTestUtils.setField(LogExceptionInterceptor.class, "loggerFactory", iLoggerFactory);

        given(iLoggerFactory.getLogger(LogExceptionInterceptor.class.getName())).willReturn(logger);
        given(throwable.getMessage()).willReturn(message);

        // When
        logExceptionInterceptor.log(throwable);

        // Then
        verify(logger).error(message, throwable);
    }

    @Test
    public void log_throwable_issues_error_message_using_classname_of_the_LogExceptionInterceptor_when_thestacktrace_is_empty() {
        // Given
        String message = "Test throwable message";
        ReflectionTestUtils.setField(LogExceptionInterceptor.class, "loggerFactory", iLoggerFactory);

        given(throwable.getStackTrace()).willReturn(new StackTraceElement[0]);
        given(iLoggerFactory.getLogger(LogExceptionInterceptor.class.getName())).willReturn(logger);
        given(throwable.getMessage()).willReturn(message);

        // When
        logExceptionInterceptor.log(throwable);

        // Then
        verify(logger).error(message, throwable);
    }

    @Test
    public void log_throwable_issues_error_message_using_classname_of_the_class_that_threw_the_throwable() {
        // Given
        String message = "Test throwable message";
        ReflectionTestUtils.setField(LogExceptionInterceptor.class, "loggerFactory", iLoggerFactory);

        given(throwable.getStackTrace()).
                willReturn(new StackTraceElement[]{
                        new StackTraceElement(LogExceptionInterceptorTest.class.getName(), "testMethodName", "testFileName", 1)});
        given(iLoggerFactory.getLogger(LogExceptionInterceptorTest.class.getName())).willReturn(logger);
        given(throwable.getMessage()).willReturn(message);

        // When
        logExceptionInterceptor.log(throwable);

        // Then
        verify(logger).error(message, throwable);
    }
}
