package com.collabrium.groups.management.domain.model.commands;

public record CreateInvitationCommand(
    Long userId,
    Long groupId
) {
}