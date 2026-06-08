package com.collabrium.groups.management.domain.exceptions;

public class UserNotFoundException extends RuntimeException {

  public UserNotFoundException(
      String message
  ) {

    super(message);
  }

  public static UserNotFoundException forId(
      Long userId
  ) {

    return new UserNotFoundException(
        "User with id " + userId + " was not found"
    );
  }
}