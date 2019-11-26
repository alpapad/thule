package uk.co.serin.thule.spring.boot.starter.security.oauth2.autoconfiguration;

import org.springframework.boot.context.properties.ConfigurationProperties;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@ConfigurationProperties("thule.shared.oauth2")
@Getter
@Setter
@ToString
public class Oauth2Properties {
    private boolean enabled = true;
    private String signingKey = "gmjtdvNVmQRz8bzw6ae";
}
