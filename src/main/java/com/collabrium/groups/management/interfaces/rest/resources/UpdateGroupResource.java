package com.collabrium.groups.management.interfaces.rest.resources;

public record UpdateGroupResource(
    String name,
    String imgUrl,
    String description
) {
}