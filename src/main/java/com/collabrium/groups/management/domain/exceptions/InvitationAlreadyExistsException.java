package com.collabrium.groups.management.domain.exceptions;

import com.collabrium.groups.management.domain.model.valueobjects.MemberId;

public class InvitationAlreadyExistsException extends RuntimeException {

  public InvitationAlreadyExistsException(MemberId memberId) {

    super(
        "Member with id " +
            memberId.value() +
            " already has a pending invitation"
    );
  }
}