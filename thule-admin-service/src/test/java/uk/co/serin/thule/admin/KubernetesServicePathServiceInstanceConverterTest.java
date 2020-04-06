package uk.co.serin.thule.admin;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.cloud.client.ServiceInstance;

import java.net.URI;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
public class KubernetesServicePathServiceInstanceConverterTest {
    protected static final String LOCALHOST_URI = "http://localhost";
    @Mock
    private ServiceInstance serviceInstance;
    @InjectMocks
    private KubernetesServicePathServiceInstanceConverter sut;

    @Test
    public void given_a_service_path_metadata_when_getServiceUrl_then_required_service_url_is_returned() {
        // Given
        var expectedUri = URI.create(LOCALHOST_URI + "/thule-test-service/");

        given(serviceInstance.getMetadata()).willReturn(Map.of("service-path", "thule-test-service"));
        given(serviceInstance.getUri()).willReturn(URI.create(LOCALHOST_URI));

        // When
        var serviceUrl = sut.getServiceUrl(serviceInstance);

        //Then
        assertThat(serviceUrl).isEqualTo(expectedUri);
    }

    @Test
    public void given_no_service_path_metadata_when_getServiceUrl_then_default_service_url_is_returned() {
        // Given
        var expectedUri = URI.create(LOCALHOST_URI + "/");

        given(serviceInstance.getMetadata()).willReturn(Map.of());
        given(serviceInstance.getUri()).willReturn(URI.create(LOCALHOST_URI));

        // When
        var serviceUrl = sut.getServiceUrl(serviceInstance);

        //Then
        assertThat(serviceUrl).isEqualTo(expectedUri);
    }
}