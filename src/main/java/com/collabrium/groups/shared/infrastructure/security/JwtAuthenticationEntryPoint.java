package com.collabrium.groups.shared.infrastructure.security;

import com.collabrium.groups.shared.interfaces.rest.resources.AuthenticationErrorResource;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.security.SignatureException;
import java.time.Instant;
import java.util.List;

@Component
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {

  private static final Logger LOGGER =
      LoggerFactory.getLogger(JwtAuthenticationEntryPoint.class);

  private final ObjectMapper objectMapper = new ObjectMapper();

  @Override
  public void commence(
      HttpServletRequest request,
      HttpServletResponse response,
      AuthenticationException authException
  ) throws IOException {

    LOGGER.error("Authentication error", authException);

    String code = "AUTH-001";
    String message = "Unauthorized";
    List<String> details;

    Throwable cause = authException.getCause();

    String exceptionMessage =
        authException.getMessage() != null
            ? authException.getMessage()
            : "Unknown authentication error";

    LOGGER.error("Exception message: {}", exceptionMessage);

    if (cause != null) {
      LOGGER.error("Cause class: {}", cause.getClass().getName());
      LOGGER.error("Cause message: {}", cause.getMessage());
    }

    // =========================================
    // TOKEN MISSING
    // =========================================
    if (exceptionMessage.contains("Full authentication is required")) {

      code = "AUTH-001";

      message = "JWT token is missing";

      details = List.of(
          "Authorization header is required",
          "Expected format: Bearer <token>"
      );
    }

    // =========================================
    // TOKEN EXPIRED
    // =========================================
    else if (
        exceptionMessage.contains("Jwt expired") ||
            cause instanceof ExpiredJwtException
    ) {

      code = "AUTH-003";

      message = "Expired JWT token";

      details = List.of(
          "The JWT token has expired",
          "Authenticate again"
      );
    }

    // =========================================
    // INVALID SIGNATURE
    // =========================================
    else if (
        exceptionMessage.contains("Signed JWT rejected") ||
            exceptionMessage.contains("Invalid signature") ||
            cause instanceof SignatureException
    ) {

      code = "AUTH-004";

      message = "Invalid JWT signature";

      details = List.of(
          "JWT signature validation failed",
          "Check JWT_SECRET consistency"
      );
    }

    // =========================================
    // INVALID AUDIENCE
    // =========================================
    else if (
        exceptionMessage.contains("The required audience is missing")
    ) {

      code = "AUTH-005";

      message = "Invalid JWT audience";

      details = List.of(
          "JWT audience validation failed",
          "Check JWT_AUDIENCE value"
      );
    }

    // =========================================
    // INVALID ISSUER
    // =========================================
    else if (
        exceptionMessage.contains("issuer")
    ) {

      code = "AUTH-006";

      message = "Invalid JWT issuer";

      details = List.of(
          "JWT issuer validation failed",
          "Check JWT_ISSUER value"
      );
    }

    // =========================================
    // MALFORMED TOKEN
    // =========================================
    else if (
        exceptionMessage.contains("Malformed") ||
            cause instanceof MalformedJwtException
    ) {

      code = "AUTH-002";

      message = "Malformed JWT token";

      details = List.of(
          "The JWT token format is invalid",
          "Expected format: Bearer <token>"
      );
    }

    // =========================================
    // BAD JWT
    // =========================================
    else if (
        exceptionMessage.contains("JWT")
    ) {

      code = "AUTH-007";

      message = "Invalid JWT token";

      details = List.of(
          exceptionMessage
      );
    }

    // =========================================
    // UNKNOWN
    // =========================================
    else {

      code = "AUTH-999";

      message = "Unknown authentication error";

      details = List.of(
          exceptionMessage
      );
    }

    AuthenticationErrorResource error =
        new AuthenticationErrorResource(
            Instant.now().toString(),
            HttpServletResponse.SC_UNAUTHORIZED,
            "Unauthorized",
            code,
            message,
            request.getRequestURI(),
            details
        );

    response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
    response.setContentType("application/json");

    objectMapper.writeValue(response.getOutputStream(), error);
  }
}