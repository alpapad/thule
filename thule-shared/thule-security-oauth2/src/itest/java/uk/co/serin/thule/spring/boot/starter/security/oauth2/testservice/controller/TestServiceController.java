package uk.co.serin.thule.spring.boot.starter.security.oauth2.testservice.controller;

import com.fasterxml.jackson.databind.ObjectMapper;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import uk.co.serin.thule.security.context.DelegatingSecurityContextHolder;

import java.util.Map;

@RestController
public class TestServiceController {
    private DelegatingSecurityContextHolder delegatingSecurityContextHolder;
    private ObjectMapper objectMapper;

    public TestServiceController(DelegatingSecurityContextHolder delegatingSecurityContextHolder, ObjectMapper objectMapper) {
        this.delegatingSecurityContextHolder = delegatingSecurityContextHolder;
        this.objectMapper = objectMapper;
    }

    @GetMapping(value = "/authentication")
    public Map<String, Object> authentication() {
        return objectMapper.convertValue(delegatingSecurityContextHolder.getAuthentication(), Map.class);
    }
}
