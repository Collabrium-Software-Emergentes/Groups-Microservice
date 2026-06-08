package com.collabrium.groups.shared.infrastructure.clients.tasks.resources;

public record MemberResource(
    Long id,
    String username,
    String name,
    String surname,
    String imgUrl
) {
}