package com.gohenry.utils.aop;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.reflect.MethodSignature;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.lang.reflect.Proxy;
import java.util.Collection;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;

@RunWith(MockitoJUnitRunner.class)
public class TracePublicMethodsInterceptorTest {
    private final TracePublicMethodsInterceptor sut = new TracePublicMethodsInterceptor();
    @Mock
    private ProceedingJoinPoint proceedingJoinPoint;
    @Mock
    private MethodSignature signature;

    @Test
    public void empty_pointcuts_do_not_throw_an_exception() {
        // Given

        // When
        sut.pointcutDefinitionBasedOnAnnotationForMethod();
        sut.pointcutDefinitionBasedOnAnnotationForType();
        sut.publicMethod();

        // Then (no exception is a success)
    }

    @Test
    public void trace_method_on_proxy_target() throws Throwable {
        // Given
        final Object expectedReturnValue = "done";
        Object proxyTarget = Proxy.newProxyInstance(Collection.class.getClassLoader(), new Class<?>[]{Collection.class}, (proxy, method, args) -> null);
        given(proceedingJoinPoint.getTarget()).willReturn(proxyTarget);
        given(proceedingJoinPoint.getSignature()).willReturn(signature);
        given(proceedingJoinPoint.getArgs()).willReturn(new Object[0]);
        given(proceedingJoinPoint.proceed()).willReturn(expectedReturnValue);

        // When
        Object actualReturnValue = sut.trace(proceedingJoinPoint);

        // Then
        assertThat(actualReturnValue).isEqualTo(expectedReturnValue);
    }

    @Test(expected = RuntimeException.class)
    public void trace_method_when_it_throws_a_runtime_exception() throws Throwable {
        // Given
        given(proceedingJoinPoint.getTarget()).willReturn(new Object());
        given(proceedingJoinPoint.getSignature()).willReturn(signature);
        given(proceedingJoinPoint.getArgs()).willReturn(new Object[0]);
        given(proceedingJoinPoint.proceed()).willThrow(new IllegalStateException());

        // When
        sut.trace(proceedingJoinPoint);

        // Then (see expected exception in annotation)
    }

    @Test(expected = Throwable.class)
    public void trace_method_when_it_throws_a_throwable() throws Throwable {
        // Given
        given(proceedingJoinPoint.getTarget()).willReturn(new Object());
        given(proceedingJoinPoint.getSignature()).willReturn(signature);
        given(proceedingJoinPoint.getArgs()).willReturn(new Object[0]);
        given(proceedingJoinPoint.proceed()).willThrow(new Throwable());

        // When
        sut.trace(proceedingJoinPoint);

        // Then (see expected exception in annotation)
    }

    @Test
    public void trace_method_with_no_args() throws Throwable {
        traceMethod();
    }

    private void traceMethod(final Object... args) throws Throwable {
        // Given
        final Object expectedReturnValue = "done";
        given(proceedingJoinPoint.getTarget()).willReturn(new Object());
        given(proceedingJoinPoint.getSignature()).willReturn(signature);
        given(proceedingJoinPoint.getArgs()).willReturn(args);
        given(proceedingJoinPoint.proceed()).willReturn(expectedReturnValue);

        // When
        Object actualReturnValue = sut.trace(proceedingJoinPoint);

        // Then
        assertThat(actualReturnValue).isEqualTo(expectedReturnValue);
    }

    @Test
    public void trace_method_with_one_arg() throws Throwable {
        traceMethod("arg1");
    }

    @Test
    public void trace_method_with_two_args() throws Throwable {
        traceMethod("arg1", "arg2");
    }

    @Test
    public void trace_method_with_void_return() throws Throwable {
        // Given
        given(proceedingJoinPoint.getTarget()).willReturn(new Object());
        given(proceedingJoinPoint.getSignature()).willReturn(signature);
        given(proceedingJoinPoint.getArgs()).willReturn(new Object[]{});
        given(signature.getReturnType()).willReturn(void.class);

        // When
        Object actualReturnValue = sut.trace(proceedingJoinPoint);

        // Then
        assertThat(actualReturnValue).isNull();
    }
}
