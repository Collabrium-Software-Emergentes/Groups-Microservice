package com.collabrium.groups.management.domain.exceptions;

public class GroupNotFoundException extends RuntimeException {

  public GroupNotFoundException(String message) {
    super(message);
  }

  public static GroupNotFoundException forId(Long groupId) {

    return new GroupNotFoundException(
        "Group not found with id: " + groupId
    );
  }

  public static GroupNotFoundException forLeader(Long leaderId) {

    return new GroupNotFoundException(
        "Group not found for leader id: " + leaderId
    );
  }

  public static GroupNotFoundException forUser(Long userId) {

    return new GroupNotFoundException(
        "No group found for user id: " + userId
    );
  }
}