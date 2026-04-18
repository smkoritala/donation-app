package com.snowbird.donations.security.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Getter
@Setter
@ConfigurationProperties(prefix = "app.jwt")
public class JwtProperties {

    private boolean enabled = true;
    private String issuer;
    private String secret;
    private String principalClaim = "sub";
    private String rolesClaim = "roles";
    private String nameClaim = "name";
    private String emailClaim = "email";
    private long allowedClockSkewSeconds = 60L;
}
