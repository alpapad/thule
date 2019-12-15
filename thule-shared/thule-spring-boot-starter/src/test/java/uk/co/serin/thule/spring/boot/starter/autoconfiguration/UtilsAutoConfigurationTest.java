package uk.co.serin.thule.spring.boot.starter.autoconfiguration;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.junit.MockitoJUnitRunner;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(MockitoJUnitRunner.class)
public class UtilsAutoConfigurationTest {
    @InjectMocks
    private UtilsAutoConfiguration sut;

    @Test
    public void when_logExceptionInterceptor_then_an_instance_is_instantiated() {
        // When
        var logExceptionInterceptor = sut.logExceptionInterceptor();

        // Then
        assertThat(logExceptionInterceptor).isNotNull();
    }

    @Test
    public void when_tracePublicMethodsInterceptor_then_an_instance_is_instantiated() {
        // When
        var tracePublicMethodsInterceptor = sut.tracePublicMethodsInterceptor();

        // Then
        assertThat(tracePublicMethodsInterceptor).isNotNull();
    }
}