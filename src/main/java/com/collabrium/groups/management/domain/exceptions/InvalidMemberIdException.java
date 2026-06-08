package com.collabrium.groups.management.domain.exceptions;

public class InvalidMemberIdException extends RuntimeException {

  public InvalidMemberIdException(String message) {
    super(message);
  }

  public static InvalidMemberIdException forNull() {
    return new InvalidMemberIdException("Member ID cannot be null");
  }

  public static InvalidMemberIdException forZeroOrNegative(Long value) {
    return new InvalidMemberIdException(
        String.format("Member ID cannot be zero or negative. Received: %d", value)
    );
  }

  public static InvalidMemberIdException forInvalidValue(Long value, String reason) {
    return new InvalidMemberIdException(
        String.format("Invalid member ID: %d. %s", value, reason)
    );
  }
}