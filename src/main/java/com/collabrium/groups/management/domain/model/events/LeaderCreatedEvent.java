package com.collabrium.groups.management.domain.model.events;

public record LeaderCreatedEvent(
    Long userId,
    Long leaderId
) {
}