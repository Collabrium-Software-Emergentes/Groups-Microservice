package com.collabrium.groups.shared.interfaces.rest.resources;

import java.time.LocalDateTime;

public record ErrorResource(
    String error,
    String message,
    int status,
    LocalDateTime timestamp
) {
}