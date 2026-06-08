package com.collabrium.groups.management.domain.model.valueobjects;

import com.collabrium.groups.management.domain.exceptions.InvalidImageUrlException;
import jakarta.persistence.Embeddable;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.regex.Pattern;

@Embeddable
public record ImgUrl(String imgUrl) {

  private static final Pattern URL_PATTERN = Pattern.compile(
      "^(https?|ftp)://[^\\s/$.?#].[^\\s]*$",
      Pattern.CASE_INSENSITIVE
  );

  public ImgUrl {
    validateImageUrl(imgUrl);
  }

  private static void validateImageUrl(String url) {
    if (url == null) {
      throw InvalidImageUrlException.forNull();
    }

    if (url.isBlank()) {
      throw InvalidImageUrlException.forBlank();
    }

    if (!isValidUrlFormat(url)) {
      throw InvalidImageUrlException.forInvalidFormat(url);
    }
  }

  private static boolean isValidUrlFormat(String url) {
    try {
      URI uri = new URI(url);
      return uri.isAbsolute() && (uri.getScheme().equals("http") || uri.getScheme().equals("https"));
    } catch (URISyntaxException e) {
      return false;
    }
  }

  public static ImgUrl of(String url) {
    return new ImgUrl(url);
  }

  @Override
  public String toString() {
    return imgUrl;
  }
}