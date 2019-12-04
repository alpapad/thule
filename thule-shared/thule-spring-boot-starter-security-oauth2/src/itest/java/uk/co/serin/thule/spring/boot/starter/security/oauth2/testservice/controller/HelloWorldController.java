package uk.co.serin.thule.spring.boot.starter.security.oauth2.testservice.controller;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import uk.co.serin.thule.security.context.DelegatingSecurityContextHolder;
import uk.co.serin.thule.spring.boot.starter.security.oauth2.ResourceServerAutoConfigurationIntTest;

import java.util.HashSet;

import static org.assertj.core.api.Assertions.assertThat;

@RestController
public class HelloWorldController {
    private DelegatingSecurityContextHolder delegatingSecurityContextHolder;

    public HelloWorldController(DelegatingSecurityContextHolder delegatingSecurityContextHolder) {
        this.delegatingSecurityContextHolder = delegatingSecurityContextHolder;
    }

    @GetMapping(value = "/hello")
    public String hello() {
        var actualGrantedAuthorities = new HashSet<GrantedAuthority>(delegatingSecurityContextHolder.getAuthentication().getAuthorities());
        var actualUserName = delegatingSecurityContextHolder.getAuthentication().getName();
        var actualUserId = delegatingSecurityContextHolder.getUserAuthenticationDetails().orElseThrow().getUserId();

        assertThat(actualGrantedAuthorities).containsExactlyElementsOf(ResourceServerAutoConfigurationIntTest.GRANTED_AUTHORITIES);
        assertThat(actualUserId).isEqualTo(ResourceServerAutoConfigurationIntTest.USER_ID);
        assertThat(actualUserName).isEqualTo(ResourceServerAutoConfigurationIntTest.USER_NAME);
        return "Hello World";
    }
}
