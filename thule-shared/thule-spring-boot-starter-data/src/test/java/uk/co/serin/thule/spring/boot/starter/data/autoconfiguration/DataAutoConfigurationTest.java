package uk.co.serin.thule.spring.boot.starter.data.autoconfiguration;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import uk.co.serin.thule.security.context.DelegatingSecurityContextHolder;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(MockitoJUnitRunner.class)
public class DataAutoConfigurationTest {
    @Mock
    private DelegatingSecurityContextHolder delegatingSecurityContextHolder;
    @InjectMocks
    private DataAutoConfiguration sut;

    @Test
    public void when_springSecurityAuditorAware_then_an_instance_is_instantiated() {
        // When
        var springSecurityAuditorAware = sut.springSecurityAuditorAware(delegatingSecurityContextHolder);

        // Then
        assertThat(springSecurityAuditorAware).isNotNull();
    }
}