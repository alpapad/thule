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
public class ApplicationConfigurationTest {
    private ApplicationConfiguration applicationConfiguration = new ApplicationConfiguration();
    @Mock
    private ApplicationContext applicationContext;
    @Mock
    private ObjectPostProcessor objectPostProcessor;
    @InjectMocks
    private WebSecurity webSecurity;

    @Test
    public void configure_adds_request_mappings() {
        // Given

        // When
        applicationConfiguration.configure(webSecurity);

        // Then
        List ignoredRequests = List.class.cast(ReflectionTestUtils.getField(webSecurity, "ignoredRequests"));
        assertThat(ignoredRequests).hasSize(1);
    }

    @Before
    public void setUp() {
        webSecurity.setApplicationContext(applicationContext);
    }
}