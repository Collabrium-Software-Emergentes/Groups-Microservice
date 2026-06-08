package com.collabrium.groups.management.domain.exceptions;

import com.collabrium.groups.management.domain.model.valueobjects.MemberId;

public class InvitationNotFoundException extends RuntimeException {

  public InvitationNotFoundException(String message) {
    super(message);
  }

  public static InvitationNotFoundException forMember(
      MemberId memberId
  ) {

    return new InvitationNotFoundException(
        "Invitation not found for member id: " +
            memberId.value()
    );
  }

  public static InvitationNotFoundException forId(
      Long invitationId
  ) {

    return new InvitationNotFoundException(
        "Invitation not found with id: " +
            invitationId
    );
  }
}