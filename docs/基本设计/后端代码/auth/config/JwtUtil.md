# JwtUtil

```java
package com.example.auth.config;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;

@Component
public class JwtUtil {

    private final SecretKey key;
    private final long defaultExpiry;
    private final long rememberMeExpiry;

    public JwtUtil(@Value("${jwt.secret}") String secret,
                   @Value("${jwt.expiry-seconds:1800}") long defaultExpiry,
                   @Value("${jwt.remember-me-expiry-seconds:86400}") long rememberMeExpiry) {
        this.key = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
        this.defaultExpiry = defaultExpiry;
        this.rememberMeExpiry = rememberMeExpiry;
    }

    public String generateToken(String userId, String username, boolean rememberMe) {
        long expiry = rememberMe ? rememberMeExpiry : defaultExpiry;
        Date now = new Date();
        return Jwts.builder()
                .subject(userId)
                .claim("username", username)
                .issuedAt(now)
                .expiration(new Date(now.getTime() + expiry * 1000))
                .audience().add("api").and()
                .signWith(key)
                .compact();
    }

    public Claims parseToken(String token) {
        return Jwts.parser()
                .verifyWith(key)
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    public long getExpirySeconds(boolean rememberMe) {
        return rememberMe ? rememberMeExpiry : defaultExpiry;
    }
}
```
