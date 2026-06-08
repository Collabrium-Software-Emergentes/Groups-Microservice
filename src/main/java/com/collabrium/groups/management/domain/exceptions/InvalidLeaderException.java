package com.collabrium.groups.management.domain.exceptions;

public class InvalidLeaderException extends RuntimeException {

  public InvalidLeaderException(String message) {
    super(message);
  }

  public static InvalidLeaderException forUserIsNotLeader(
      Long userId
  ) {

    return new InvalidLeaderException(
        "User with id " + userId + " is not a leader"
    );
  }
}