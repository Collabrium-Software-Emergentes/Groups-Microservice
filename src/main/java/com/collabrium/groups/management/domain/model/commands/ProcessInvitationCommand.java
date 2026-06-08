package com.collabrium.groups.management.domain.model.commands;

public record ProcessInvitationCommand(
    Long userId,
    Long invitationId,
    boolean accept
) {
}