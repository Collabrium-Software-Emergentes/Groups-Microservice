package com.collabrium.groups.management.domain.exceptions;

public class LeaderConsistencyException extends RuntimeException {

  public LeaderConsistencyException(String message) {
    super(message);
  }

  public static LeaderConsistencyException forMismatchBetweenRequestsAndTime() {
    return new LeaderConsistencyException(
        "Inconsistency detected: solved requests count doesn't match average solution time calculation"
    );
  }
}