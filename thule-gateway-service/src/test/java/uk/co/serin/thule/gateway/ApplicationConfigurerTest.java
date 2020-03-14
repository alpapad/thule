package uk.co.serin.thule.gateway;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.oauth2.client.registration.ReactiveClientRegistrationRepository;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.security.web.server.header.XFrameOptionsServerHttpHeadersWriter;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;

@RunWith(MockitoJUnitRunner.class)
public class ApplicationConfigurerTest {
    @Mock
    private ServerHttpSecurity.AuthorizeExchangeSpec.Access access;
    @Mock
    private ServerHttpSecurity.AuthorizeExchangeSpec authorizeExchangeSpec;
    @Mock
    private ReactiveClientRegistrationRepository clientRegistrationRepository;
    @Mock
    private ServerHttpSecurity.CsrfSpec csrfSpec;
    @Captor
    private ArgumentCaptor<Customizer<ServerHttpSecurity.LogoutSpec>> customizerArgumentCaptor;
    @Mock
    private ServerHttpSecurity.HeaderSpec.FrameOptionsSpec frameOptionsSpec;
    @Mock
    private ServerHttpSecurity.HeaderSpec headerSpec;
    @Mock
    private ServerHttpSecurity.LogoutSpec logoutSpec;
    @Mock
    private ServerHttpSecurity.OAuth2LoginSpec oAuth2LoginSpec;
    @Mock
    private SecurityWebFilterChain securityWebFilterChain;
    @Mock
    private ServerHttpSecurity serverHttpSecurity;
    @InjectMocks
    private ApplicationConfigurer sut;

    @Test
    public void when_creating_springSecurityFilterChain_then_an_instance_is_created() {
        // Given
        given(serverHttpSecurity.oauth2Login()).willReturn(oAuth2LoginSpec);

        given(serverHttpSecurity.logout(customizerArgumentCaptor.capture())).willReturn(serverHttpSecurity);

        given(serverHttpSecurity.authorizeExchange()).willReturn(authorizeExchangeSpec);
        given(authorizeExchangeSpec.anyExchange()).willReturn(access);
        given(access.authenticated()).willReturn(authorizeExchangeSpec);

        given(serverHttpSecurity.headers()).willReturn(headerSpec);
        given(headerSpec.frameOptions()).willReturn(frameOptionsSpec);
        given(frameOptionsSpec.mode(XFrameOptionsServerHttpHeadersWriter.Mode.SAMEORIGIN)).willReturn(headerSpec);

        given(serverHttpSecurity.csrf()).willReturn(csrfSpec);
        given(csrfSpec.disable()).willReturn(serverHttpSecurity);

        given(serverHttpSecurity.build()).willReturn(securityWebFilterChain);

        // When
        var securityWebFilterChain = sut.springSecurityFilterChain(serverHttpSecurity, clientRegistrationRepository);

        // Then
        var customizer = customizerArgumentCaptor.getValue();
        customizer.customize(logoutSpec); // Test lambda
        assertThat(securityWebFilterChain).isNotNull();
    }
}