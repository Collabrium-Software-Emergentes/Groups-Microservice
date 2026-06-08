package com.collabrium.groups.management.interfaces.rest.resources;

public record LeaderResource(
    Long id,
    String averageSolutionTime,
    Integer solvedRequests
) {
}