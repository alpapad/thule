package com.gohenry.spring.boot.starter.oauth2.autoconfiguration;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.security.oauth2.provider.token.AccessTokenConverter;
import org.springframework.security.oauth2.provider.token.ResourceServerTokenServices;
import org.springframework.security.oauth2.provider.token.TokenStore;

import static com.gohenry.test.assertj.GoHenryAssertions.assertThat;
import static org.mockito.BDDMockito.given;

@RunWith(MockitoJUnitRunner.class)
public class Oauth2AutoConfigurationTest {
    @InjectMocks
    private Oauth2AutoConfiguration sut;
    @Mock
    private Oauth2Properties oauth2Properties;

    @Test
    public void access_token_converter_is_configured() {
        //Given
        given(oauth2Properties.getSigningKey()).willReturn("signingKey");

        //When
        AccessTokenConverter accessTokenConverter = sut.jwtAccessTokenConverter();

        //Then
        assertThat(accessTokenConverter).isNotNull();
    }

    @Test
    public void class_under_test_instantiates_successfully() {
        // Given

        // When
        Oauth2AutoConfiguration gohenryOauthAutoConfiguration = new Oauth2AutoConfiguration(new Oauth2Properties());

        // Then
        assertThat(gohenryOauthAutoConfiguration).isNotNull();
    }

    @Test
    public void token_services_is_configured() {
        //Given
        given(oauth2Properties.getSigningKey()).willReturn("signingKey");

        //When
        ResourceServerTokenServices resourceServerTokenServices = sut.defaultTokenServices();

        //Then
        assertThat(resourceServerTokenServices).isNotNull();
    }

    @Test
    public void token_store_is_configured() {
        //Given
        given(oauth2Properties.getSigningKey()).willReturn("signingKey");

        //When
        TokenStore tokenStore = sut.tokenStore();

        //Then
        assertThat(tokenStore).isNotNull();
    }

}