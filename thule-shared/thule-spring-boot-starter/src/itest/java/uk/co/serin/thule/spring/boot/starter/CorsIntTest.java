package uk.co.serin.thule.spring.boot.starter;


import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.reactive.server.WebTestClient;

import uk.co.serin.thule.spring.boot.starter.testservice.Application;

@ActiveProfiles("itest")
@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class CorsIntTest {
    @Autowired
    private WebTestClient webTestClient;

    @Test
    public void given_an_access_control_request_method_http_header_when_making_an_options_request_then_access_should_be_granted_with_no_body_in_the_response() {
        // When
        webTestClient.options().uri("/hello")
                     .header(HttpHeaders.ACCESS_CONTROL_REQUEST_METHOD, HttpMethod.PATCH.name())
                     .header(HttpHeaders.ORIGIN, "http://localhost")
                     .exchange()

                     // Then
                     .expectStatus().isOk();
    }
}