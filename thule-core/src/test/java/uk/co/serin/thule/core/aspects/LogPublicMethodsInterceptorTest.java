package uk.co.serin.thule.core.aspects;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.lang.reflect.Proxy;
import java.util.Collection;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;

@RunWith(MockitoJUnitRunner.class)
public class LogPublicMethodsInterceptorTest {
    private final LogPublicMethodsInterceptor logPublicMethodsInterceptor = new LogPublicMethodsInterceptor();
    @Mock
    private ProceedingJoinPoint proceedingJoinPoint;
    @Mock
    private Signature signature;

    @Test
    public void emptyPointcuts() {
        // Given

        // When
        logPublicMethodsInterceptor.pointcutDefinitionBasedOnAnnotationForMethod();
        logPublicMethodsInterceptor.pointcutDefinitionBasedOnAnnotationForType();
        logPublicMethodsInterceptor.publicMethod();

        // Then (no exception is a success)
    }

    @Test
    public void logMethodOnProxyTarget() throws Throwable {
        // Given
        final Object expectedReturnValue = "done";
        Object proxyTarget = Proxy.newProxyInstance(Collection.class.getClassLoader(), new Class<?>[]{Collection.class}, (proxy, method, args) -> null);
        given(proceedingJoinPoint.getTarget()).willReturn(proxyTarget);
        given(proceedingJoinPoint.getSignature()).willReturn(signature);
        given(proceedingJoinPoint.getArgs()).willReturn(new Object[0]);
        given(proceedingJoinPoint.proceed()).willReturn(expectedReturnValue);

        // When
        Object actualReturnValue = logPublicMethodsInterceptor.log(proceedingJoinPoint);

        // Then
        assertThat(actualReturnValue).isEqualTo(expectedReturnValue);
    }

    @Test(expected = RuntimeException.class)
    public void logMethodWhenItThrowsARuntimeException() throws Throwable {
        // Given
        given(proceedingJoinPoint.getTarget()).willReturn(new Object());
        given(proceedingJoinPoint.getSignature()).willReturn(signature);
        given(proceedingJoinPoint.getArgs()).willReturn(new Object[0]);
        given(proceedingJoinPoint.proceed()).willThrow(new IllegalStateException());

        // When
        logPublicMethodsInterceptor.log(proceedingJoinPoint);

        // Then (see expected exception in annotation)
    }

    @Test(expected = Throwable.class)
    public void logMethodWhenItThrowsAThrowable() throws Throwable {
        // Given
        given(proceedingJoinPoint.getTarget()).willReturn(new Object());
        given(proceedingJoinPoint.getSignature()).willReturn(signature);
        given(proceedingJoinPoint.getArgs()).willReturn(new Object[0]);
        given(proceedingJoinPoint.proceed()).willThrow(new Throwable());

        // When
        logPublicMethodsInterceptor.log(proceedingJoinPoint);

        // Then (see expected exception in annotation)
    }

    @Test
    public void logMethodWithNoArgs() throws Throwable {
        logMethod();
    }

    @Test
    public void logMethodWithOneArg() throws Throwable {
        logMethod("arg1");
    }

    @Test
    public void logMethodWithTwoArgs() throws Throwable {
        logMethod("arg1", "arg2");
    }

    private void logMethod(final Object... args) throws Throwable {
        // Given
        final Object expectedReturnValue = "done";
        given(proceedingJoinPoint.getTarget()).willReturn(new Object());
        given(proceedingJoinPoint.getSignature()).willReturn(signature);
        given(proceedingJoinPoint.getArgs()).willReturn(args);
        given(proceedingJoinPoint.proceed()).willReturn(expectedReturnValue);

        // When
        Object actualReturnValue = logPublicMethodsInterceptor.log(proceedingJoinPoint);

        // Then
        assertThat(actualReturnValue).isEqualTo(expectedReturnValue);
    }
}
