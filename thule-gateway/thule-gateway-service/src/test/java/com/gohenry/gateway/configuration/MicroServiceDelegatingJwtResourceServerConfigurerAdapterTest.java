package com.gohenry.gateway.configuration;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.security.config.annotation.ObjectPostProcessor;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;

import java.util.Map;

@RunWith(MockitoJUnitRunner.class)
public class MicroServiceDelegatingJwtResourceServerConfigurerAdapterTest {
    @Mock
    private AuthenticationManagerBuilder authenticationManagerBuilder;
    @InjectMocks
    private HttpSecurity httpSecurity;
    @InjectMocks
    private MicroServiceDelegatingJwtResourceServerConfigurerAdapter microServiceDelegatingJwtResourceServerConfigurerAdapter;
    @Mock
    private ObjectPostProcessor objectPostProcessor;
    @Mock
    private Map<Class<? extends Object>, Object> sharedObjects;

    @Test
    public void configure_configures_token_servicess() throws Exception {
        //Given

        //When
        microServiceDelegatingJwtResourceServerConfigurerAdapter.configure(httpSecurity);

        //Then
    }
}