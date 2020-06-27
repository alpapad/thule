package uk.co.serin.thule.spring.boot.starter.resourceserver.testservice.controller;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import uk.co.serin.thule.resourceserver.context.JwtUserAuthenticationSecurityContext;
import uk.co.serin.thule.spring.boot.starter.resourceserver.ResourceServerAutoConfigurationIntTest;

import java.util.HashSet;

import static org.assertj.core.api.Assertions.assertThat;

@RestController
public class HelloWorldController {
    private JwtUserAuthenticationSecurityContext jwtUserAuthenticationSecurityContext;

    public HelloWorldController(JwtUserAuthenticationSecurityContext jwtUserAuthenticationSecurityContext) {
        this.jwtUserAuthenticationSecurityContext = jwtUserAuthenticationSecurityContext;
    }

    @GetMapping(value = "/hello")
    public String hello() {
        var actualGrantedAuthorities = new HashSet<GrantedAuthority>(
                jwtUserAuthenticationSecurityContext.getSecurityContextHolder().getAuthentication().getAuthorities());
        var actualUserName = jwtUserAuthenticationSecurityContext.getSecurityContextHolder().getAuthentication().getName();
        var actualUserId = jwtUserAuthenticationSecurityContext.getUserAuthentication().orElseThrow().getUserId();

        assertThat(actualGrantedAuthorities).containsExactlyElementsOf(ResourceServerAutoConfigurationIntTest.GRANTED_AUTHORITIES);
        assertThat(actualUserName).isEqualTo(ResourceServerAutoConfigurationIntTest.USER_NAME);
        assertThat(actualUserId).isEqualTo(ResourceServerAutoConfigurationIntTest.USER_ID);
        return "Hello World";
    }
}
