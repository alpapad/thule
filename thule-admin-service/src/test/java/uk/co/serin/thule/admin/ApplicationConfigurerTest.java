package uk.co.serin.thule.admin;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.junit.MockitoJUnitRunner;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(MockitoJUnitRunner.class)
public class ApplicationConfigurerTest {
    @InjectMocks
    private ApplicationConfigurer sut;

    @Test
    public void when_creating_serviceInstanceConverter_then_an_instance_is_created() {
        // When
        var kubernetesServicePathServiceInstanceConverter = sut.serviceInstanceConverter();

        // Then
        assertThat(kubernetesServicePathServiceInstanceConverter).isNotNull();
    }
}