package com.thule.gateway.zuul;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.netflix.zuul.filters.ZuulProperties;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

@ActiveProfiles("itest")
@RunWith(SpringRunner.class)
@SpringBootTest
public class ZuulRoutingIntTest {
    @Autowired
    private ZuulProperties zuulProperties;

    @Test
    public void when_ignored_services_include_business_or_unknown_services_then_fail_validation() {
        // Given

        // When
        Set<String> zuulIgnoredServices = zuulProperties.getIgnoredServices();

        // Then
        assertThat(zuulIgnoredServices).containsExactly("thule-admin-service",
                "thule-configuration-service",
                "thule-discovery-service",
                "thule-gateway-service");
    }
}