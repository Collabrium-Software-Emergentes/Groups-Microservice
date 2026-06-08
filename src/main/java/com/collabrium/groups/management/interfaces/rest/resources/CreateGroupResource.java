package com.collabrium.groups.management.interfaces.rest.resources;

public record CreateGroupResource(
    String name,
    String imgUrl,
    String description
) {
}