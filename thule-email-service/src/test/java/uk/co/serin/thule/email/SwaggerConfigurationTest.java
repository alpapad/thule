package uk.co.serin.thule.email;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.boot.info.BuildProperties;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;

@RunWith(MockitoJUnitRunner.class)
public class SwaggerConfigurationTest {
    @Mock
    private BuildProperties buildProperties;
    @InjectMocks
    private SwaggerConfiguration sut;

    @Test
    public void when_creating_a_docket_then_a_docket_is_returned() {
        //Given
        given(buildProperties.getVersion()).willReturn("1.0.0");

        //When
        var docket = sut.api();

        //Then
        assertThat(docket).isNotNull();
    }
}