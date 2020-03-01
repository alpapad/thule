package uk.co.serin.thule.gateway;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.boot.actuate.autoconfigure.security.reactive.EndpointRequest;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

@RunWith(MockitoJUnitRunner.class)
public class ApplicationConfigurerTest {
    @Mock
    private ServerHttpSecurity.AuthorizeExchangeSpec.Access access;
    @Mock
    private ServerHttpSecurity.AnonymousSpec anonymousSpec;
    @Mock
    private ServerHttpSecurity.AuthorizeExchangeSpec authorizeExchangeSpec;
    @Captor
    private ArgumentCaptor<Customizer<ServerHttpSecurity.AuthorizeExchangeSpec>> customizerArgumentCaptor;
    @Mock
    private SecurityWebFilterChain securityWebFilterChain;
    @Mock
    private ServerHttpSecurity serverHttpSecurity;
    @InjectMocks
    private ApplicationConfigurer sut;

    @Test
    public void when_creating_springSecurityFilterChain_then_an_instance_is_created() {
        // Given
        given(serverHttpSecurity.authorizeExchange(customizerArgumentCaptor.capture())).willReturn(serverHttpSecurity);

        given(authorizeExchangeSpec.matchers(any(EndpointRequest.EndpointServerWebExchangeMatcher.class))).willReturn(access);
        given(access.permitAll()).willReturn(authorizeExchangeSpec);

        given(authorizeExchangeSpec.pathMatchers("/thule-*-service/**")).willReturn(access);
        given(access.permitAll()).willReturn(authorizeExchangeSpec);

        given(serverHttpSecurity.anonymous()).willReturn(anonymousSpec);

        given(serverHttpSecurity.build()).willReturn(securityWebFilterChain);

        // When
        sut.springSecurityFilterChain(serverHttpSecurity);

        // Then
        var customizer = customizerArgumentCaptor.getValue();
        customizer.customize(authorizeExchangeSpec); // Test lambda
    }
}