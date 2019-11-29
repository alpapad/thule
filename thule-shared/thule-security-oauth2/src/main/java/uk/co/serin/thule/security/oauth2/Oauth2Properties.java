package uk.co.serin.thule.security.oauth2;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Service;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
@ConfigurationProperties("thule.shared.oauth2")
@Getter
@NoArgsConstructor
@Service
@Setter
public class Oauth2Properties {
    @Builder.Default
    private Resource resource = new Resource();

    @AllArgsConstructor(access = AccessLevel.PRIVATE)
    @Builder
    @Getter
    @NoArgsConstructor
    @Setter
    public static class Resource {
        @Builder.Default
        private boolean enabled = true;
    }
}
