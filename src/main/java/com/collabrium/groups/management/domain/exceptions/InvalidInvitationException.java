package com.collabrium.groups.management.domain.exceptions;

public class InvalidInvitationException extends RuntimeException {

  public InvalidInvitationException(String message) {
    super(message);
  }

  public static InvalidInvitationException forNullMemberId() {
    return new InvalidInvitationException("Cannot create invitation: Member ID is required");
  }

  public static InvalidInvitationException forNullGroup() {
    return new InvalidInvitationException("Cannot create invitation: Group is required");
  }
}