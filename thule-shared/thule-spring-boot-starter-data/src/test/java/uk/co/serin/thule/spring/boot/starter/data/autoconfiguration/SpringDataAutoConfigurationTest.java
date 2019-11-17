package uk.co.serin.thule.spring.boot.starter.data.autoconfiguration;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import uk.co.serin.thule.security.DelegatingSecurityContextHolder;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(MockitoJUnitRunner.class)
public class SpringDataAutoConfigurationTest {
    @Mock
    private DelegatingSecurityContextHolder delegatingSecurityContextHolder;
    @InjectMocks
    private SpringDataAutoConfiguration sut;

    @Test
    public void when_spring_security_auditor_aware_then_a_spring_security_auditor_aware_is_created() {
        // When
        var springSecurityAuditorAware = sut.springSecurityAuditorAware(delegatingSecurityContextHolder);

        // Then
        assertThat(springSecurityAuditorAware).isNotNull();
    }
}