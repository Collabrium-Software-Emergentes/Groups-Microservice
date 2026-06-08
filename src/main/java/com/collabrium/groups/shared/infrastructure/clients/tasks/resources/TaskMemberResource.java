package com.collabrium.groups.shared.infrastructure.clients.tasks.resources;

public record TaskMemberResource(
    Long id,
    String name,
    String surname,
    String urlImage
) {
}