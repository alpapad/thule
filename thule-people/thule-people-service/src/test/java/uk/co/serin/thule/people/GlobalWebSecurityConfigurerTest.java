package uk.co.serin.thule.people;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.security.config.annotation.ObjectPostProcessor;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;

import java.util.Map;

@RunWith(MockitoJUnitRunner.class)
public class GlobalWebSecurityConfigurerTest {
    @Mock
    private AuthenticationManagerBuilder authenticationManagerBuilder;
    @InjectMocks
    private GlobalWebSecurityConfigurer globalWebSecurityConfigurer;
    @InjectMocks
    private HttpSecurity httpSecurity;
    @Mock
    private ObjectPostProcessor objectPostProcessor;
    @Mock
    private Map<Class<? extends Object>, Object> sharedObjects;

    @Test
    public void when_configure_http_security_then_no_exceptions_are_thrown() throws Exception {
        // Given

        // When
        globalWebSecurityConfigurer.configure(httpSecurity);

        // Then (the test is successful if no exception has been thrown!!)
    }
}