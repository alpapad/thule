package uk.co.serin.thule.utils.trace;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.slf4j.ILoggerFactory;
import org.slf4j.Logger;
import org.springframework.test.util.ReflectionTestUtils;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class LogExceptionInterceptorTest {
    @Mock
    private ILoggerFactory iLoggerFactory;
    @Mock
    private Logger logger;
    @InjectMocks
    private LogExceptionInterceptor sut;
    @Mock
    private Throwable throwable;

    @Test
    public void given_throwable_a_empty_stacktrace_when_exception_is_logged_then_logger_of_class_in_stacktrace_is_used() {
        // Given
        var message = "Test throwable message";
        ReflectionTestUtils.setField(LogExceptionInterceptor.class, "loggerFactory", iLoggerFactory);

        given(throwable.getStackTrace())
                .willReturn(new StackTraceElement[]{new StackTraceElement(LogExceptionInterceptorTest.class.getName(), "testMethodName", "testFileName", 1)});
        given(iLoggerFactory.getLogger(LogExceptionInterceptorTest.class.getName())).willReturn(logger);
        given(throwable.getMessage()).willReturn(message);

        // When
        sut.log(throwable);

        // Then
        verify(logger).error(message, throwable);
    }

    @Test
    public void given_throwable_with_empty_stacktrace_when_exception_is_logged_then_logger_of_LogExceptionInterceptor_is_used() {
        // Given
        var message = "Test throwable message";
        ReflectionTestUtils.setField(LogExceptionInterceptor.class, "loggerFactory", iLoggerFactory);

        given(throwable.getStackTrace()).willReturn(new StackTraceElement[0]);
        given(iLoggerFactory.getLogger(LogExceptionInterceptor.class.getName())).willReturn(logger);
        given(throwable.getMessage()).willReturn(message);

        // When
        sut.log(throwable);

        // Then
        verify(logger).error(message, throwable);
    }

    @Test
    public void given_throwable_with_null_stacktrace_when_exception_is_logged_then_logger_of_LogExceptionInterceptor_is_used() {
        // Given
        var message = "Test throwable message";
        ReflectionTestUtils.setField(LogExceptionInterceptor.class, "loggerFactory", iLoggerFactory);

        given(iLoggerFactory.getLogger(LogExceptionInterceptor.class.getName())).willReturn(logger);
        given(throwable.getMessage()).willReturn(message);

        // When
        sut.log(throwable);

        // Then
        verify(logger).error(message, throwable);
    }

    @Test
    public void when_pointcuts_are_called_then_no_exception_is_thrown() {
        var throwable = catchThrowable(() -> {
            sut.pointcutDefinitionBasedOnAnnotationForMethod();
            sut.pointcutDefinitionBasedOnAnnotationForType();
        });

        // Then
        assertThat(throwable).isNull();
    }
}