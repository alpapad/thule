package uk.co.serin.thule.spring.boot.starter.security.autoconfiguration;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.junit.MockitoJUnitRunner;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(MockitoJUnitRunner.class)
public class SpringSecurityAutoConfigurationTest {
    @InjectMocks
    private SpringSecurityAutoConfiguration sut;

    @Test
    public void when_delegating_security_context_holder_then_a_delegating_security_context_holder_is_created() {
        // When
        var delegatingSecurityContextHolder = sut.delegatingSecurityContextHolder();

        // Then
        assertThat(delegatingSecurityContextHolder).isNotNull();
    }
}