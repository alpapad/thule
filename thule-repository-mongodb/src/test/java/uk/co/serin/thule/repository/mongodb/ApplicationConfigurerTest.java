package uk.co.serin.thule.repository.mongodb;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.data.mongodb.core.mapping.event.ValidatingMongoEventListener;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(MockitoJUnitRunner.class)
public class ApplicationConfigurerTest {
    @InjectMocks
    private ApplicationConfigurer applicationConfigurer;

    @Test
    public void applicationConfigurer_instantiates_without_an_exception() {
        // Given

        // When
        ApplicationConfigurer applicationConfigurer = new ApplicationConfigurer();

        // Then
        assertThat(applicationConfigurer).isNotNull();
    }

    @Test
    public void localValidatorFactoryBean_is_configured() {
        // Given

        // When
        LocalValidatorFactoryBean localValidatorFactoryBean = applicationConfigurer.localValidatorFactoryBean();

        // Then
        assertThat(localValidatorFactoryBean).isNotNull();
    }

    @Test
    public void validatingMongoEventListener_is_configured() {
        // Given

        // When
        ValidatingMongoEventListener validatingMongoEventListener = applicationConfigurer.validatingMongoEventListener();

        // Then
        assertThat(validatingMongoEventListener).isNotNull();
    }
}