package com.collabrium.groups.management.interfaces.rest.resources;

public record GroupWithLeaderResource(
    Long id,
    String name,
    String imgUrl,
    String description,
    String code,
    Integer memberCount,
    Long leaderId
) {
}