package com.collabrium.groups.management.domain.model.valueobjects;

import com.collabrium.groups.management.domain.exceptions.InvalidMemberIdException;
import jakarta.persistence.Embeddable;

@Embeddable
public record MemberId(Long value) {

  public MemberId {
    validateMemberId(value);
  }

  private static void validateMemberId(Long value) {
    if (value == null) {
      throw InvalidMemberIdException.forNull();
    }

    if (value <= 0) {
      throw InvalidMemberIdException.forZeroOrNegative(value);
    }
  }

  public static MemberId of(Long value) {
    return new MemberId(value);
  }

  public static MemberId fromString(String value) {
    try {
      Long parsedValue = Long.parseLong(value);
      return new MemberId(parsedValue);
    } catch (NumberFormatException e) {
      throw InvalidMemberIdException.forInvalidValue(
          null,
          String.format("Cannot parse '%s' as a valid number", value)
      );
    }
  }

  public boolean isEqualTo(Long otherValue) {
    return this.value.equals(otherValue);
  }

  public boolean isGreaterThan(MemberId other) {
    return this.value > other.value;
  }

  public boolean isLessThan(MemberId other) {
    return this.value < other.value;
  }

  @Override
  public String toString() {
    return String.valueOf(value);
  }
}