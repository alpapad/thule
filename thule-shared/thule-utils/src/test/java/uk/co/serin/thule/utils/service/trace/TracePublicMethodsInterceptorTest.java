package uk.co.serin.thule.utils.service.trace;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.reflect.MethodSignature;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.lang.reflect.Proxy;
import java.util.Collection;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.mockito.BDDMockito.given;

@RunWith(MockitoJUnitRunner.class)
public class TracePublicMethodsInterceptorTest {
    @Mock
    private ProceedingJoinPoint proceedingJoinPoint;
    @Mock
    private MethodSignature signature;
    @InjectMocks
    private TracePublicMethodsInterceptor sut;

    @Test
    public void given_no_arguments_when_trace_then_return_value() throws Throwable {
        traceMethod();
    }

    private void traceMethod(final Object... args) throws Throwable {
        // Given
        Object expectedReturnValue = "done";
        given(proceedingJoinPoint.getTarget()).willReturn(new Object());
        given(proceedingJoinPoint.getSignature()).willReturn(signature);
        given(proceedingJoinPoint.getArgs()).willReturn(args);
        given(proceedingJoinPoint.proceed()).willReturn(expectedReturnValue);

        // When
        var actualReturnValue = sut.trace(proceedingJoinPoint);

        // Then
        assertThat(actualReturnValue).isEqualTo(expectedReturnValue);
    }

    @Test
    public void given_one_argument_when_trace_then_return_value() throws Throwable {
        traceMethod("arg1");
    }

    @Test
    public void given_proceed_throws_an_illegal_state_exception_when_trace_then_illegal_state_exception_is_thrown() throws Throwable {
        // Given
        given(proceedingJoinPoint.getTarget()).willReturn(new Object());
        given(proceedingJoinPoint.getSignature()).willReturn(signature);
        given(proceedingJoinPoint.getArgs()).willReturn(new Object[0]);
        given(proceedingJoinPoint.proceed()).willThrow(new IllegalStateException());

        // When
        var throwable = catchThrowable(() -> sut.trace(proceedingJoinPoint));

        // Then
        assertThat(throwable).isInstanceOf(RuntimeException.class);
    }

    @Test
    public void given_proceed_throws_an_throwable_when_trace_then_a_throwable_is_thrown() throws Throwable {
        // Given
        given(proceedingJoinPoint.getTarget()).willReturn(new Object());
        given(proceedingJoinPoint.getSignature()).willReturn(signature);
        given(proceedingJoinPoint.getArgs()).willReturn(new Object[0]);
        given(proceedingJoinPoint.proceed()).willThrow(new Throwable());

        // When
        var throwable = catchThrowable(() -> sut.trace(proceedingJoinPoint));

        // Then
        assertThat(throwable).isInstanceOf(Throwable.class);
    }

    @Test
    public void given_proxy_target_when_trace_then_return_value() throws Throwable {
        // Given
        Object expectedReturnValue = "done";
        var proxyTarget = Proxy.newProxyInstance(Collection.class.getClassLoader(), new Class<?>[]{Collection.class}, (proxy, method, args) -> null);
        given(proceedingJoinPoint.getTarget()).willReturn(proxyTarget);
        given(proceedingJoinPoint.getSignature()).willReturn(signature);
        given(proceedingJoinPoint.getArgs()).willReturn(new Object[0]);
        given(proceedingJoinPoint.proceed()).willReturn(expectedReturnValue);

        // When
        var actualReturnValue = sut.trace(proceedingJoinPoint);

        // Then
        assertThat(actualReturnValue).isEqualTo(expectedReturnValue);
    }

    @Test
    public void given_two_arguments_when_trace_then_return_value() throws Throwable {
        traceMethod("arg1", "arg2");
    }

    @Test
    public void given_void_return_value_when_trace_then_return_value_is_null() {
        // Given
        given(proceedingJoinPoint.getTarget()).willReturn(new Object());
        given(proceedingJoinPoint.getSignature()).willReturn(signature);
        given(proceedingJoinPoint.getArgs()).willReturn(new Object[]{});
        given(signature.getReturnType()).willReturn(void.class);

        // When
        var actualReturnValue = sut.trace(proceedingJoinPoint);

        // Then
        assertThat(actualReturnValue).isNull();
    }

    @Test
    public void when_pointcuts_are_called_then_no_exception_is_thrown() {
        // When
        var throwable = catchThrowable(() -> {
            sut.pointcutDefinitionBasedOnAnnotationForMethod();
            sut.pointcutDefinitionBasedOnAnnotationForType();
            sut.publicMethod();
        });

        // Then
        assertThat(throwable).isNull();
    }
}
