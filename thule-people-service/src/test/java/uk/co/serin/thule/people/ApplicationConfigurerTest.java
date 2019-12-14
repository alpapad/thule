package uk.co.serin.thule.people;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.security.oauth2.client.resource.OAuth2ProtectedResourceDetails;

import uk.co.serin.thule.security.context.DelegatingSecurityContextHolder;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(MockitoJUnitRunner.class)
public class ApplicationConfigurerTest {
    @Mock
    private DelegatingSecurityContextHolder delegatingSecurityContextHolder;
    @Mock
    private OAuth2ProtectedResourceDetails oAuth2ProtectedResourceDetails;
    @InjectMocks
    private ApplicationConfigurer sut;

    @Test
    public void when_jwtPropagatingOAuth2FeignRequestInterceptor_then_an_instance_is_instantiated() {
        // When
        var jwtPropagatingOAuth2FeignRequestInterceptor =
                sut.jwtPropagatingOAuth2FeignRequestInterceptor(delegatingSecurityContextHolder, oAuth2ProtectedResourceDetails);

        // Then
        assertThat(jwtPropagatingOAuth2FeignRequestInterceptor).isNotNull();
    }
}