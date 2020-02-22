package uk.co.serin.thule.keycloak.feign.testservice.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloWorldController {
    @GetMapping(value = "/hello")
    public String hello() {
        return "Hello World";
    }
}
