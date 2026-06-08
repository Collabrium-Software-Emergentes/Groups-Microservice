package com.collabrium.groups.management.domain.exceptions;

public class InvalidImageUrlException extends RuntimeException {

  public InvalidImageUrlException(String message) {
    super(message);
  }

  public static InvalidImageUrlException forNull() {
    return new InvalidImageUrlException("Image URL cannot be null");
  }

  public static InvalidImageUrlException forBlank() {
    return new InvalidImageUrlException("Image URL cannot be blank or empty");
  }

  public static InvalidImageUrlException forInvalidFormat(String url) {
    return new InvalidImageUrlException(
        String.format("Invalid image URL format: '%s'. Expected a valid URL (http:// or https://)", url)
    );
  }
}