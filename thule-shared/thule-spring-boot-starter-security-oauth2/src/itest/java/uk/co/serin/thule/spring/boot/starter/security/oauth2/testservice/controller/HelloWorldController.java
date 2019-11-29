package uk.co.serin.thule.spring.boot.starter.security.oauth2.testservice.controller;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import uk.co.serin.thule.security.oauth2.context.DelegatingSecurityContextHolder;
import uk.co.serin.thule.spring.boot.starter.security.oauth2.ResourceServerIntTest;

import java.util.HashSet;

import static org.assertj.core.api.Assertions.assertThat;

@RestController
public class HelloWorldController {
    private DelegatingSecurityContextHolder delegatingSecurityContextHolder = new DelegatingSecurityContextHolder();

    @GetMapping(value = "/assert-correct-security-context")
    public String assertCorrectSecurityContext() {
        var actualGrantedAuthorities = new HashSet<GrantedAuthority>(delegatingSecurityContextHolder.getAuthentication().getAuthorities());
        var actualUserName = delegatingSecurityContextHolder.getAuthentication().getName();
        var actualUserId = delegatingSecurityContextHolder.getUserAuthenticationDetails().orElseThrow().getUserId();

        assertThat(actualGrantedAuthorities).containsExactlyElementsOf(ResourceServerIntTest.GRANTED_AUTHORITIES);
        assertThat(actualUserId).isEqualTo(ResourceServerIntTest.USER_ID);
        assertThat(actualUserName).isEqualTo(ResourceServerIntTest.USER_NAME);
        return "Hello World";
    }
}
