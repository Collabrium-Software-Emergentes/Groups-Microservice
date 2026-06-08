package com.collabrium.groups.management.domain.exceptions;

public class MemberNotFoundException extends RuntimeException {

  public MemberNotFoundException(String message) {
    super(message);
  }

  public static MemberNotFoundException forUserId(
      Long userId
  ) {

    return new MemberNotFoundException(
        "No member associated with user id " + userId + " was found"
    );
  }

  public static MemberNotFoundException forMemberId(
      Long memberId
  ) {

    return new MemberNotFoundException(
        "Member with id " + memberId + " was not found"
    );
  }
}