package uk.co.serin.thule.repository.mongodb;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.junit.MockitoJUnitRunner;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(MockitoJUnitRunner.class)
public class ApplicationConfigurerTest {
    @InjectMocks
    private ApplicationConfigurer applicationConfigurer;

    @Test
    public void localValidatorFactoryBean_is_configured() {
        // When
        var localValidatorFactoryBean = applicationConfigurer.localValidatorFactoryBean();

        // Then
        assertThat(localValidatorFactoryBean).isNotNull();
    }

    @Test
    public void validatingMongoEventListener_is_configured() {
        // When
        var validatingMongoEventListener = applicationConfigurer.validatingMongoEventListener();

        // Then
        assertThat(validatingMongoEventListener).isNotNull();
    }

    @Test
    public void when_default_constructor_is_invoked_then_an_instance_is_instantiated() {
        // When
        var applicationConfigurer = new ApplicationConfigurer();

        // Then
        assertThat(applicationConfigurer).isNotNull();
    }
}