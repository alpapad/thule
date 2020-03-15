package uk.co.serin.thule.authentication.keycloak;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Service;

import java.net.URI;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
@ConfigurationProperties("thule.keycloak")
@Getter
@NoArgsConstructor
@Service
@Setter
public class KeycloakProperties {
    @Builder.Default
    private Admin admin = new Admin();
    @Builder.Default
    private URI baseUrl = URI.create("http://localhost");
    @Builder.Default
    private String realm = "master";

    @AllArgsConstructor(access = AccessLevel.PRIVATE)
    @Builder
    @Getter
    @NoArgsConstructor
    @Setter
    public static class Admin {
        @Builder.Default
        private String clientId = "admin-cli";
        private String password = "admin";
        private String username = "admin";
    }
}
