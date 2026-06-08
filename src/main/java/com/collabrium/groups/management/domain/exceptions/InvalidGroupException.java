package com.collabrium.groups.management.domain.exceptions;

public class InvalidGroupException extends RuntimeException {

  public InvalidGroupException(String message) {
    super(message);
  }

  public static InvalidGroupException forNullName() {
    return new InvalidGroupException("Group name cannot be null");
  }

  public static InvalidGroupException forNullDescription() {
    return new InvalidGroupException("Group description cannot be null");
  }

  public static InvalidGroupException forNullLeader() {
    return new InvalidGroupException("Group leader cannot be null");
  }

  public static InvalidGroupException forNullCode() {
    return new InvalidGroupException("Group code cannot be null");
  }

  public static InvalidGroupException forEmptyName() {
    return new InvalidGroupException("Group name cannot be empty");
  }

  public static InvalidGroupException forEmptyDescription() {
    return new InvalidGroupException("Group description cannot be empty");
  }

  public static InvalidGroupException forInvalidMemberCount() {
    return new InvalidGroupException("Member count cannot be negative");
  }

  public static InvalidGroupException forMemberCountDecrementWhenZero() {
    return new InvalidGroupException("Cannot decrease member count when it is already zero");
  }

  public static InvalidGroupException forLeaderWithoutGroup(
      Long leaderId
  ) {

    return new InvalidGroupException(
        "Leader with id " + leaderId + " does not belong to any group"
    );
  }

  public static InvalidGroupException forGroupNotFound(
      Long groupId
  ) {

    return new InvalidGroupException(
        "Group with id " + groupId + " was not found"
    );
  }

  public static InvalidGroupException forMemberNotBelongingToGroup(
      Long memberId,
      Long groupId
  ) {

    return new InvalidGroupException(
        "Member with id " + memberId +
            " does not belong to group with id " + groupId
    );
  }
}