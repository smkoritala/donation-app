package com.snowbird.donations.security.util;

import com.snowbird.donations.common.exception.UnauthorizedException;
import com.snowbird.donations.security.config.JwtProperties;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import java.nio.charset.StandardCharsets;
import java.sql.Date;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.crypto.SecretKey;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class JwtTokenService {

    private final JwtProperties jwtProperties;

    public Claims parseToken(String token) {
        try {
            Claims claims = Jwts.parser()
                    .verifyWith(getSigningKey())
                    .clockSkewSeconds(jwtProperties.getAllowedClockSkewSeconds())
                    .build()
                    .parseSignedClaims(token)
                    .getPayload();

            validateIssuer(claims);
            return claims;
        } catch (JwtException | IllegalArgumentException ex) {
            throw new UnauthorizedException("Invalid JWT token");
        }
    }

    public String extractPrincipal(Claims claims) {
        String principalClaim = jwtProperties.getPrincipalClaim();

        if ("sub".equalsIgnoreCase(principalClaim)) {
            String subject = claims.getSubject();
            if (subject == null || subject.isBlank()) {
                throw new UnauthorizedException("JWT subject is missing");
            }
            return subject;
        }

        Object value = claims.get(principalClaim);
        if (value == null) {
            throw new UnauthorizedException("JWT principal claim is missing");
        }
        return String.valueOf(value);
    }

    public String extractName(Claims claims) {
        Object value = claims.get(jwtProperties.getNameClaim());
        return value != null ? String.valueOf(value) : null;
    }

    public String extractEmail(Claims claims) {
        Object value = claims.get(jwtProperties.getEmailClaim());
        return value != null ? String.valueOf(value) : null;
    }

    public List<String> extractRoles(Claims claims) {
        Object rawRoles = claims.get(jwtProperties.getRolesClaim());
        if (rawRoles == null) {
            return List.of();
        }

        if (rawRoles instanceof String roleString) {
            if (roleString.isBlank()) {
                return List.of();
            }
            return List.of(roleString.split(","));
        }

        if (rawRoles instanceof Collection<?> collection) {
            List<String> roles = new ArrayList<>();
            for (Object item : collection) {
                if (item != null) {
                    roles.add(String.valueOf(item));
                }
            }
            return roles;
        }

        return List.of(String.valueOf(rawRoles));
    }

    private void validateIssuer(Claims claims) {
        String expectedIssuer = jwtProperties.getIssuer();
        if (expectedIssuer == null || expectedIssuer.isBlank()) {
            return;
        }

        String tokenIssuer = claims.getIssuer();
        if (tokenIssuer == null || !expectedIssuer.equals(tokenIssuer)) {
            throw new UnauthorizedException("Invalid JWT issuer");
        }
    }

    private SecretKey getSigningKey() {
        String secret = jwtProperties.getSecret();
        if (secret == null || secret.isBlank()) {
            throw new UnauthorizedException("JWT secret is not configured");
        }
        return Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
    }
    
    public String generateToken(String userId, String name, String email, List<String> roles) {
        Map<String, Object> claims = new HashMap<>();
        claims.put(jwtProperties.getRolesClaim(), roles);
        claims.put(jwtProperties.getNameClaim(), name);
        claims.put(jwtProperties.getEmailClaim(), email);

        Instant now = Instant.now();

        return Jwts.builder()
                .claims(claims)
                .subject(userId)
                .issuer(jwtProperties.getIssuer())
                .issuedAt(Date.from(now))
                .expiration(Date.from(now.plusSeconds(3600)))
                .signWith(getSigningKey())
                .compact();
    }
}
