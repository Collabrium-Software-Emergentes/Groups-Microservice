package com.collabrium.groups.management.domain.exceptions;

public class InvalidLeaderOperationException extends RuntimeException {

  public InvalidLeaderOperationException(String message) {
    super(message);
  }

  public static InvalidLeaderOperationException forNullSolutionDuration() {
    return new InvalidLeaderOperationException("Solution duration cannot be null");
  }

  public static InvalidLeaderOperationException forNegativeSolutionDuration(long seconds) {
    return new InvalidLeaderOperationException(
        String.format("Solution duration cannot be negative. Received: %d seconds", seconds)
    );
  }

  public static InvalidLeaderOperationException forInvalidSolvedRequests(Integer requests) {
    return new InvalidLeaderOperationException(
        String.format("Solved requests cannot be negative. Received: %d", requests)
    );
  }
}