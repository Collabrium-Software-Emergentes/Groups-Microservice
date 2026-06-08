package com.collabrium.groups.management.domain.model.valueobjects;

import com.collabrium.groups.management.domain.exceptions.InvalidCodeException;
import jakarta.persistence.Embeddable;

import java.security.SecureRandom;
import java.util.regex.Pattern;

@Embeddable
public record GroupCode(String value) {

  private static final SecureRandom RANDOM = new SecureRandom();
  private static final String ALPHANUMERIC_CHARS = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ";
  private static final int CODE_LENGTH = 9;
  private static final Pattern CODE_PATTERN = Pattern.compile("[0-9A-Z]{" + CODE_LENGTH + "}");

  public GroupCode {
    validateCode(value);
  }

  private static void validateCode(String code) {
    if (code == null) {
      throw InvalidCodeException.forNull();
    }

    if (code.length() != CODE_LENGTH) {
      throw InvalidCodeException.forInvalidLength(code);
    }

    if (!CODE_PATTERN.matcher(code).matches()) {
      throw InvalidCodeException.forInvalidFormat(code);
    }
  }

  public static GroupCode generate() {

    StringBuilder code = new StringBuilder(CODE_LENGTH);

    for (int i = 0; i < CODE_LENGTH; i++) {

      int randomIndex = RANDOM.nextInt(ALPHANUMERIC_CHARS.length());
      code.append(ALPHANUMERIC_CHARS.charAt(randomIndex));
    }

    return new GroupCode(code.toString());
  }

  public static GroupCode fromString(String code) {

    return new GroupCode(code);
  }

  @Override
  public String toString() {

    return value;
  }
}