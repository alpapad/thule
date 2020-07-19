package uk.co.serin.thule.people.controller;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;

import uk.co.serin.thule.people.service.PeopleService;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class PhotographControllerTest {
    @Mock
    private PeopleService peopleService;
    @InjectMocks
    private PhotographController photographController;

    @Test
    void when_getting_a_photograph_then_photograph_is_returned() {
        // Given
        var id = 12345678;
        var expectedPhotograph = "photograph".getBytes();
        given(peopleService.photograph(id)).willReturn(expectedPhotograph);

        // When
        var responseEntity = photographController.getPhotograph(id);

        // Then
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(responseEntity.getBody()).isEqualTo(expectedPhotograph);
    }
}
