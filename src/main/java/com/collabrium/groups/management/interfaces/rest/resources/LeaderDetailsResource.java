package com.collabrium.groups.management.interfaces.rest.resources;

public record LeaderDetailsResource(
    Long id,
    String username,
    String name,
    String surname,
    String imgUrl,
    String email,
    String averageSolutionTime,
    Integer solvedRequests
) {
}