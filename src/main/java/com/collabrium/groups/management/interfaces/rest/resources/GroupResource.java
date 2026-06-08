package com.collabrium.groups.management.interfaces.rest.resources;

public record GroupResource(
    Long id,
    String name,
    String imgUrl,
    String description,
    String code,
    Integer memberCount
) {
}