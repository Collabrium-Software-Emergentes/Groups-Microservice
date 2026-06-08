package com.collabrium.groups.management.domain.exceptions;

public class InvalidCodeException extends RuntimeException {

  public InvalidCodeException(String message) {
    super(message);
  }

  public static InvalidCodeException forNull() {
    return new InvalidCodeException("Group code cannot be null");
  }

  public static InvalidCodeException forInvalidFormat(String code) {
    return new InvalidCodeException(
        String.format("Invalid group code format: '%s'. Expected: 9 alphanumeric characters (0-9, A-Z)", code)
    );
  }

  public static InvalidCodeException forInvalidLength(String code) {
    return new InvalidCodeException(
        String.format("Group code must be exactly 9 characters. Received: %d characters",
            code != null ? code.length() : 0)
    );
  }
}