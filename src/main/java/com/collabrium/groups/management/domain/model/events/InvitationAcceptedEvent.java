package com.collabrium.groups.management.domain.model.events;

public record InvitationAcceptedEvent(
    Long groupId,
    Long memberId
) {
}