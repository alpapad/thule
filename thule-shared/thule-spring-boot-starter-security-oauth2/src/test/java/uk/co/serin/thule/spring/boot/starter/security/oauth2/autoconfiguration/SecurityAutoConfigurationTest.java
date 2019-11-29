package uk.co.serin.thule.spring.boot.starter.security.oauth2.autoconfiguration;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.junit.MockitoJUnitRunner;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(MockitoJUnitRunner.class)
public class SecurityAutoConfigurationTest {
    @InjectMocks
    private SecurityAutoConfiguration sut;

    @Test
    public void when_delegatingSecurityContextHolder_then_an_instance_is_instantiated() {
        // When
        var delegatingSecurityContextHolder = sut.delegatingSecurityContextHolder();

        // Then
        assertThat(delegatingSecurityContextHolder).isNotNull();
    }
}