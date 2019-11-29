package uk.co.serin.thule.spring.boot.starter.security.oauth2.autoconfiguration;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.junit.MockitoJUnitRunner;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(MockitoJUnitRunner.class)
public class ResourceServerAutoConfigurationTest {
    @InjectMocks
    private ResourceServerAutoConfiguration sut;

    @Test
    public void when_jwtAccessTokenCustomizer_then_an_instance_is_instantiated() {
        // When
        var accessTokenConverter = sut.jwtAccessTokenCustomizer();

        // Then
        assertThat(accessTokenConverter).isNotNull();
    }

    @Test
    public void when_jwtResourceServerConfigurerAdapter_then_an_instance_is_instantiated() {
        // When
        var jwtResourceServerConfigurerAdapter = sut.jwtResourceServerConfigurerAdapter();

        // Then
        assertThat(jwtResourceServerConfigurerAdapter).isNotNull();
    }
}