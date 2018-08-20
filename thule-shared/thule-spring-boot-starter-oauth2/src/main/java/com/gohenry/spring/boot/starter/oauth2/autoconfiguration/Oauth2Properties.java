package com.gohenry.spring.boot.starter.oauth2.autoconfiguration;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties("gohenry.shared.oauth2")
public class Oauth2Properties {
    private boolean enabled = true;
    private String signingKey = "gmjtdvNVmQRz8bzw6ae";

    public String getSigningKey() {
        return signingKey;
    }

    public void setSigningKey(String signingKey) {
        this.signingKey = signingKey;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }
}
