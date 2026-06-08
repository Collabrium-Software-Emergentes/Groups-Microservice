package com.collabrium.groups.management.application.internal.dto;

public record TaskMemberDetailsDTO(
    Long id,
    String name,
    String surname,
    String urlImage
) {
}