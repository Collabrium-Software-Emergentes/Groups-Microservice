package com.collabrium.groups.management.application.internal.dto;

public record MemberDetailsDTO(
    Long id,
    String username,
    String name,
    String surname,
    String imgUrl
) {
}