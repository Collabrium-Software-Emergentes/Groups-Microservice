package com.collabrium.groups.management.domain.exceptions;

public class LeaderNotFoundException extends RuntimeException {

  public LeaderNotFoundException(String message) {
    super(message);
  }

  public LeaderNotFoundException(Long leaderId) {
    super("Leader with id " + leaderId + " was not found");
  }

  public static LeaderNotFoundException forId(Long leaderId) {

    return new LeaderNotFoundException(leaderId);
  }
}