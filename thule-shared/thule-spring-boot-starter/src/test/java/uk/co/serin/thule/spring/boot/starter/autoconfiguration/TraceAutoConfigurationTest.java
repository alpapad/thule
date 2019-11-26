package uk.co.serin.thule.spring.boot.starter.autoconfiguration;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.junit.MockitoJUnitRunner;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(MockitoJUnitRunner.class)
public class TraceAutoConfigurationTest {
    @InjectMocks
    private TraceAutoConfiguration sut;

    @Test
    public void when_trace_public_methods_interceptor_then_an_instance_is_instantiated() {
        // When
        var tracePublicMethodsInterceptor = sut.tracePublicMethodsInterceptor();

        // Then
        assertThat(tracePublicMethodsInterceptor).isNotNull();
    }
}