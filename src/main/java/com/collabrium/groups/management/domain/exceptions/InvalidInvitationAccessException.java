package com.collabrium.groups.management.domain.exceptions;

public class InvalidInvitationAccessException extends RuntimeException {

  public InvalidInvitationAccessException(
      Long userId,
      Long invitationId
  ) {

    super(
        "User with id " + userId +
            " cannot manage invitation with id " +
            invitationId
    );
  }
}