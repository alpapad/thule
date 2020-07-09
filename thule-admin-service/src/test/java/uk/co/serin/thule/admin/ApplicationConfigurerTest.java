package uk.co.serin.thule.admin;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
class ApplicationConfigurerTest {
    @InjectMocks
    private ApplicationConfigurer sut;

    @Test
    void when_creating_serviceInstanceConverter_then_an_instance_is_created() {
        // When
        var kubernetesServicePathServiceInstanceConverter = sut.serviceInstanceConverter();

        // Then
        assertThat(kubernetesServicePathServiceInstanceConverter).isNotNull();
    }
}