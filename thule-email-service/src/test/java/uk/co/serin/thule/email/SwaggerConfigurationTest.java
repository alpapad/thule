package uk.co.serin.thule.email;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.info.BuildProperties;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class SwaggerConfigurationTest {
    @Mock
    private BuildProperties buildProperties;
    @InjectMocks
    private SwaggerConfiguration sut;

    @Test
    void when_creating_a_docket_then_a_docket_is_returned() {
        // Given
        given(buildProperties.getVersion()).willReturn("1.0.0");

        // When
        var docket = sut.api();

        // Then
        assertThat(docket).isNotNull();
    }
}