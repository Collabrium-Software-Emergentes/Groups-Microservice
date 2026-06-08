package com.collabrium.groups.shared.infrastructure.clients.iam.resources;

public record UserOnlyResource(
    String username,
    String name,
    String surname,
    String imgUrl,
    String email,
    Long leaderId,
    Long memberId
) {
}