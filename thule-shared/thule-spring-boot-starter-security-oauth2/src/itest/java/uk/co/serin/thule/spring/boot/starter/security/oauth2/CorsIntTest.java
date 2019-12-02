package uk.co.serin.thule.spring.boot.starter.security.oauth2;


import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.util.LinkedMultiValueMap;

import uk.co.serin.thule.spring.boot.starter.security.oauth2.testservice.Application;

import static uk.co.serin.thule.test.assertj.ThuleAssertions.assertThat;


@ActiveProfiles("itest")
@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class CorsIntTest {
    @Autowired
    private TestRestTemplate testRestTemplate;

    @Test
    public void given_an_access_control_request_method_http_header_when_making_an_options_request_then_access_should_be_granted() {
        // Given
        var httpHeaders = new LinkedMultiValueMap<String, String>();
        httpHeaders.add(HttpHeaders.ACCESS_CONTROL_REQUEST_METHOD, HttpMethod.GET.name());

        // When
        var responseEntity = testRestTemplate.exchange("/hello", HttpMethod.OPTIONS, new HttpEntity<>(httpHeaders), String.class);

        // Then
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    public void given_no_access_control_request_method_http_header_when_making_an_options_request_then_access_should_be_denied() {
        // When
        var responseEntity = testRestTemplate.exchange("/hello", HttpMethod.OPTIONS, HttpEntity.EMPTY, String.class);

        // Then
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);
    }
}