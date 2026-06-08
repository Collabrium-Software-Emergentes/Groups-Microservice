package com.collabrium.groups.management.domain.exceptions;

public class GroupAlreadyExistsException extends RuntimeException {

  public GroupAlreadyExistsException(String message) {
    super(message);
  }

  public static GroupAlreadyExistsException forLeader(Long leaderId) {

    return new GroupAlreadyExistsException(
        "Leader with id " + leaderId + " already owns a group"
    );
  }
}