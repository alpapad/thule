package uk.co.serin.thule.people;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.CsrfConfigurer;
import org.springframework.security.config.annotation.web.configurers.ExpressionUrlAuthorizationConfigurer;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class PermitAnyRequestWebSecurityConfigurationTest {
    @Mock
    private ExpressionUrlAuthorizationConfigurer<HttpSecurity>.AuthorizedUrl authorizedUrl;
    @Mock
    private CsrfConfigurer<HttpSecurity> csrfConfigurer;
    @Mock
    private ExpressionUrlAuthorizationConfigurer<HttpSecurity>.ExpressionInterceptUrlRegistry expressionInterceptUrlRegistry;
    @Mock
    private HttpSecurity httpSecurity;
    @Spy
    private PermitAnyRequestWebSecurityConfiguration sut;

    @Test
    void when_configure_http_security_then_all_requests_are_permitted() throws Exception {
        // Given
        given(httpSecurity.csrf()).willReturn(csrfConfigurer);
        given(csrfConfigurer.disable()).willReturn(httpSecurity);

        given(httpSecurity.authorizeRequests()).willReturn(expressionInterceptUrlRegistry);
        given(expressionInterceptUrlRegistry.anyRequest()).willReturn(authorizedUrl);
        given(authorizedUrl.permitAll()).willReturn(expressionInterceptUrlRegistry);

        // When
        sut.configure(httpSecurity);

        // Then
        verify(authorizedUrl).permitAll();
    }
}