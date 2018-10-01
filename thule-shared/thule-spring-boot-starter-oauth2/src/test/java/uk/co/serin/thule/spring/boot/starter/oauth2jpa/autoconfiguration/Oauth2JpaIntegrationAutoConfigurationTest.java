package uk.co.serin.thule.spring.boot.starter.oauth2jpa.autoconfiguration;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class Oauth2JpaIntegrationAutoConfigurationTest {

    @Test
    public void when_creating_new_object_will_create_successfully() {

        //When
        new OAuth2JpaIntegrationAutoConfiguration();

    }

}