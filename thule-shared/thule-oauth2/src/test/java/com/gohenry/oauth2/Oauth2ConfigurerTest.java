package com.gohenry.oauth2;

import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class Oauth2ConfigurerTest {

    @Test
    public void when_oauth_configurer_is_made_then_is_not_null() {
        //Given

        //When
        Oauth2Configurer sut = new Oauth2Configurer();

        //Then
        assertThat(sut).isNotNull();
    }
}
