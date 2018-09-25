package com.gohenry.shared.oauth2;

import com.gohenry.oauth2.DelegatingSecurityContextHolder;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class SecurityContextHolderIntTest {

    @Autowired
    DelegatingSecurityContextHolder delegatingSecurityContextHolder;

    @Test
    public void when_delegating_security_context_holder_is_autowired_then_object_is_not_null() {
        //Given
        //DelegatingSecurityContextHolder is autowired

        //When

        //Then
        assertThat(delegatingSecurityContextHolder).isNotNull();
    }
}
