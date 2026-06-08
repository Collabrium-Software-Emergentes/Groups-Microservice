package com.collabrium.groups.management.application.internal.dto;

public record LeaderDetailsDTO(
    Long leaderId,
    String username,
    String name,
    String surname,
    String imgUrl,
    String email,
    String averageSolutionTime,
    Integer solvedRequests
) {
}