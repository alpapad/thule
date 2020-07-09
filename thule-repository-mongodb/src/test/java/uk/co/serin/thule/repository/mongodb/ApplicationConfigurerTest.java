package uk.co.serin.thule.repository.mongodb;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
class ApplicationConfigurerTest {
    @InjectMocks
    private ApplicationConfigurer applicationConfigurer;

    @Test
    void when_configuring_local_validator_factory_bean_then_an_instance_is_created() {
        // When
        var localValidatorFactoryBean = applicationConfigurer.localValidatorFactoryBean();

        // Then
        assertThat(localValidatorFactoryBean).isNotNull();
    }

    @Test
    void when_configuring_validating_mongo_event_listener_then_an_instance_is_created() {
        // When
        var validatingMongoEventListener = applicationConfigurer.validatingMongoEventListener();

        // Then
        assertThat(validatingMongoEventListener).isNotNull();
    }
}