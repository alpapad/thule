package uk.co.serin.thule.spring.boot.starter.oauth2.autoconfiguration;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Service;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@ConfigurationProperties("thule.shared.oauth2")
@Getter
@Setter
@Service
@ToString
public class Oauth2Properties {
    @Builder.Default
    private boolean enabled = true;
    @Builder.Default
    private String signingKey = "gmjtdvNVmQRz8bzw6ae";
}
