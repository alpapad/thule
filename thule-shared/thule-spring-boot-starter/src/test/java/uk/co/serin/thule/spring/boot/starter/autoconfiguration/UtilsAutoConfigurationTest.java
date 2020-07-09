package uk.co.serin.thule.spring.boot.starter.autoconfiguration;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
class UtilsAutoConfigurationTest {
    @InjectMocks
    private UtilsAutoConfiguration sut;

    @Test
    void when_logExceptionInterceptor_then_an_instance_is_instantiated() {
        // When
        var logExceptionInterceptor = sut.logExceptionInterceptor();

        // Then
        assertThat(logExceptionInterceptor).isNotNull();
    }

    @Test
    void when_tracePublicMethodsInterceptor_then_an_instance_is_instantiated() {
        // When
        var tracePublicMethodsInterceptor = sut.tracePublicMethodsInterceptor();

        // Then
        assertThat(tracePublicMethodsInterceptor).isNotNull();
    }
}