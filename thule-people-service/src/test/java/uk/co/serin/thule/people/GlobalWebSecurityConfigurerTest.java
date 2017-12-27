package uk.co.serin.thule.people;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.context.ApplicationContext;
import org.springframework.security.config.annotation.ObjectPostProcessor;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(MockitoJUnitRunner.class)
public class GlobalWebSecurityConfigurerTest {
    @Mock
    private ApplicationContext applicationContext;
    @InjectMocks
    private GlobalWebSecurityConfigurer globalWebSecurityConfigurer;
    @Mock
    private ObjectPostProcessor objectPostProcessor;
    @InjectMocks
    private WebSecurity webSecurity;

    @Test
    public void configurer_ignores_http_options_method() {
        // Given

        // When
        globalWebSecurityConfigurer.configure(webSecurity);

        // Then
        List ignoredRequests = List.class.cast(ReflectionTestUtils.getField(webSecurity, "ignoredRequests"));
        assertThat(ignoredRequests).hasSize(1);
    }

    @Before
    public void setUp() {
        webSecurity.setApplicationContext(applicationContext);
    }
}