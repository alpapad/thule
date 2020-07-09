package uk.co.serin.thule.feign;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientManager;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientService;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;

import uk.co.serin.thule.security.context.DelegatingSecurityContextHolder;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
class FeignJwtClientConfigurationTest {
    @Mock
    private ClientRegistrationRepository clientRegistrationRepository;
    @Mock
    private DelegatingSecurityContextHolder delegatingSecurityContextHolder;
    @Mock
    private OAuth2AuthorizedClientManager oAuth2AuthorizedClientManager;
    @Mock
    private OAuth2AuthorizedClientService oAuth2AuthorizedClientService;
    @InjectMocks
    private FeignJwtClientConfiguration sut;

    @Test
    void when_bearerAuthFeignRequestInterceptor_then_an_instance_is_instantiated() {
        // When
        var bearerAuthFeignRequestInterceptor =
                sut.bearerAuthFeignRequestInterceptor(delegatingSecurityContextHolder, oAuth2AuthorizedClientManager);

        // Then
        assertThat(bearerAuthFeignRequestInterceptor).isNotNull();
    }

    @Test
    void when_oAuth2AuthorizedClientManager_then_an_instance_is_instantiated() {
        // When
        var oAuth2AuthorizedClientManager = sut.oAuth2AuthorizedClientManager(clientRegistrationRepository, oAuth2AuthorizedClientService);

        // Then
        assertThat(oAuth2AuthorizedClientManager).isNotNull();
    }
}