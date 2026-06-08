package com.collabrium.groups.shared.infrastructure.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;

@Service
public class JwtValidationService {

  @Value("${authorization.jwt.secret}")
  private String secret;

  @Value("${authorization.jwt.issuer}")
  private String issuer;

  @Value("${authorization.jwt.audience}")
  private String audience;

  public Claims validateAndGetClaims(String token) {

    Jws<Claims> parsed = Jwts.parser()
        .verifyWith(getSigningKey())
        .requireIssuer(issuer)
        .requireAudience(audience)
        .build()
        .parseSignedClaims(token);

    return parsed.getPayload();
  }

  public String extractToken(HttpServletRequest request) {

    String authHeader =
        request.getHeader("Authorization");

    if (authHeader == null ||
        !authHeader.startsWith("Bearer ")) {
      return null;
    }

    return authHeader.substring(7);
  }

  private SecretKey getSigningKey() {

    return Keys.hmacShaKeyFor(
        secret.getBytes(StandardCharsets.UTF_8)
    );
  }
}