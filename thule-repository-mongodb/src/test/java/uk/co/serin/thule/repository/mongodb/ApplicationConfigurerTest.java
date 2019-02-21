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
    public void when_configuring_local_validator_factory_bean_then_an_instance_is_created() {
        // When
        var localValidatorFactoryBean = applicationConfigurer.localValidatorFactoryBean();

        // Then
        assertThat(localValidatorFactoryBean).isNotNull();
    }

    @Test
    public void when_configuring_validating_mongo_event_listener_then_an_instance_is_created() {
        // When
        var validatingMongoEventListener = applicationConfigurer.validatingMongoEventListener();

        // Then
        assertThat(validatingMongoEventListener).isNotNull();
    }
}