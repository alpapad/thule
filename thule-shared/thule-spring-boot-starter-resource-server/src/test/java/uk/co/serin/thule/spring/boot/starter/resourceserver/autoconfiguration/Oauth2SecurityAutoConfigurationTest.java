package uk.co.serin.thule.spring.boot.starter.resourceserver.autoconfiguration;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import uk.co.serin.thule.security.context.DelegatingSecurityContextHolder;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(MockitoJUnitRunner.class)
public class Oauth2SecurityAutoConfigurationTest {
    @Mock
    private DelegatingSecurityContextHolder delegatingSecurityContextHolder;
    @InjectMocks
    private Oauth2SecurityAutoConfiguration sut;

    @Test
    public void when_delegatingSecurityContextHolder_then_an_instance_is_instantiated() {
        // When
        var delegatingSecurityContextHolder = sut.delegatingSecurityContextHolder();

        // Then
        assertThat(delegatingSecurityContextHolder).isNotNull();
    }

    @Test
    public void when_jwtUserAuthenticationSecurityContext_then_an_instance_is_instantiated() {
        // When
        var jwtUserAuthenticationSecurityContext = sut.jwtUserAuthenticationSecurityContext(delegatingSecurityContextHolder);

        // Then
        assertThat(jwtUserAuthenticationSecurityContext).isNotNull();
    }
}