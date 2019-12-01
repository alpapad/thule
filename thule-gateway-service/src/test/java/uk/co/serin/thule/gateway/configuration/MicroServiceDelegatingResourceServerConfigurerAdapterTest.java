package uk.co.serin.thule.gateway.configuration;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.ExpressionUrlAuthorizationConfigurer;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class MicroServiceDelegatingResourceServerConfigurerAdapterTest {
    @Mock
    private ExpressionUrlAuthorizationConfigurer<HttpSecurity>.AuthorizedUrl authorizedUrl;
    @Mock
    private ExpressionUrlAuthorizationConfigurer<HttpSecurity>.ExpressionInterceptUrlRegistry expressionInterceptUrlRegistry;
    @Mock
    private HttpSecurity httpSecurity;
    @InjectMocks
    private MicroServiceDelegatingResourceServerConfigurerAdapter sut;

    @Test
    public void when_configuring_http_security_then_access_to_all_micro_service_urls_is_allowed() throws Exception {
        // Given
        given(httpSecurity.authorizeRequests()).willReturn(expressionInterceptUrlRegistry);
        given(expressionInterceptUrlRegistry.antMatchers("/thule-*-service/**")).willReturn(authorizedUrl);
        given(authorizedUrl.permitAll()).willReturn(expressionInterceptUrlRegistry);

        // When
        sut.configure(httpSecurity);

        // Then
        verify(expressionInterceptUrlRegistry).antMatchers("/thule-*-service/**");
    }
}