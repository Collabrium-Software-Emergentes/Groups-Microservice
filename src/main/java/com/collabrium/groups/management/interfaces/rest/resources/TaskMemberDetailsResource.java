package com.collabrium.groups.management.interfaces.rest.resources;

public record TaskMemberDetailsResource(
    Long id,
    String name,
    String surname,
    String urlImage
) {
}