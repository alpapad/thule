package com.gohenry.shared.oauth2jpa;

import com.gohenry.oauth2.Oauth2Utils;
import com.gohenry.shared.oauth2jpa.domain.TestAuditable;
import com.gohenry.shared.oauth2jpa.repository.TestAuditableRepository;
import com.gohenry.spring.boot.starter.oauth2.autoconfiguration.Oauth2Properties;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.DefaultOAuth2ClientContext;
import org.springframework.security.oauth2.client.OAuth2RestTemplate;
import org.springframework.security.oauth2.client.token.grant.password.ResourceOwnerPasswordResourceDetails;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ResourceServerAuditableIntTest {

    private static final String PRINCIPAL = "testPrincipal";

    private OAuth2RestTemplate oAuth2RestTemplate;
    @Autowired
    private Oauth2Properties oauth2Properties;
    @LocalServerPort
    private int port;

    @Before
    public void setUp() {
        OAuth2AccessToken jwtOauth2AccessToken = Oauth2Utils.createJwtOauth2AccessToken(
                PRINCIPAL, null, 1234567890, Collections.singleton(new SimpleGrantedAuthority("grantedAuthority")), "clientId",
                oauth2Properties.getSigningKey());
        oAuth2RestTemplate = new OAuth2RestTemplate(new ResourceOwnerPasswordResourceDetails(), new DefaultOAuth2ClientContext(jwtOauth2AccessToken));
    }


    @Test
    public void when_entity_persisted_then_lastmodifiedby_and_createdby_updated_from_oauth2_principal() {

        //When
        ResponseEntity<TestAuditable> responseEntity = oAuth2RestTemplate
                .postForEntity(String.format("http://localhost:%s/create", port), HttpEntity.EMPTY, TestAuditable.class, port);

        //Then
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);

        TestAuditable response = responseEntity.getBody();
        String createdBy = response.getCreatedBy();
        String lastModifiedBy = response.getLastModifiedBy();

        assertThat(createdBy).isEqualTo(PRINCIPAL);
        assertThat(lastModifiedBy).isEqualTo(PRINCIPAL);

    }

    @TestConfiguration
    static class ResourceServerAuditableIntTestConfiguration {

        @RestController
        public class HelloWorldController {

            @Autowired
            private TestAuditableRepository testAuditableRepository;

            @RequestMapping(value = "/create", method = RequestMethod.POST)
            public TestAuditable create() {

                TestAuditable auditable = new TestAuditable(123l);

                TestAuditable savedAuditable = testAuditableRepository.save(auditable);
                return savedAuditable;

            }

        }
    }
}

