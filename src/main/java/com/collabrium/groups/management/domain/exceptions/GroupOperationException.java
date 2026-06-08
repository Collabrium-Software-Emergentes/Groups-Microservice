package com.collabrium.groups.management.domain.exceptions;

public class GroupOperationException extends RuntimeException {

  public GroupOperationException(String message) {
    super(message);
  }

  public static GroupOperationException forDuplicateCode(String code) {
    return new GroupOperationException(
        String.format("Group with code '%s' already exists", code)
    );
  }

  public static GroupOperationException forGroupNotFound(Long id) {
    return new GroupOperationException(
        String.format("Group with id '%d' not found", id)
    );
  }

  public static GroupOperationException forGroupNotFound(String code) {
    return new GroupOperationException(
        String.format("Group with code '%s' not found", code)
    );
  }

  public static GroupOperationException forMaximumMembersReached(int maxMembers) {
    return new GroupOperationException(
        String.format("Cannot add more members. Maximum capacity of %d reached", maxMembers)
    );
  }
}