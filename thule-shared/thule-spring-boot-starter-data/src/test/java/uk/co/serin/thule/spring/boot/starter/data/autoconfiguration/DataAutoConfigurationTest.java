package uk.co.serin.thule.spring.boot.starter.data.autoconfiguration;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import uk.co.serin.thule.security.context.DelegatingSecurityContextHolder;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
class DataAutoConfigurationTest {
    @Mock
    private DelegatingSecurityContextHolder delegatingSecurityContextHolder;
    @InjectMocks
    private DataAutoConfiguration sut;

    @Test
    void when_springSecurityAuditorAware_then_an_instance_is_instantiated() {
        // When
        var springSecurityAuditorAware = sut.springSecurityAuditorAware(delegatingSecurityContextHolder);

        // Then
        assertThat(springSecurityAuditorAware).isNotNull();
    }
}