package uk.co.serin.thule.repository.mongodb;

import org.junit.Test;
import org.springframework.data.mongodb.core.mapping.event.ValidatingMongoEventListener;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;

import static org.assertj.core.api.Assertions.assertThat;

public class ApplicationTest {
    @Test
    public void defaultConstructorCreatesInstanceSuccessfully() {
        assertThat(new Application()).isNotNull();
    }

    @Test
    public void localValidatorFactoryBeanIsConfigured() {
        // Given
        Application mongodbConfiguration = new Application();

        // When
        LocalValidatorFactoryBean localValidatorFactoryBean = mongodbConfiguration.localValidatorFactoryBean();

        // Then
        assertThat(localValidatorFactoryBean).isNotNull();
    }

    @Test
    public void validationIsConfiguredngMongoEventListenerIsConfigured() {
        // Given
        Application mongodbConfiguration = new Application();

        // When
        ValidatingMongoEventListener validatingMongoEventListener = mongodbConfiguration.validatingMongoEventListener();

        // Then
        assertThat(validatingMongoEventListener).isNotNull();
    }
}