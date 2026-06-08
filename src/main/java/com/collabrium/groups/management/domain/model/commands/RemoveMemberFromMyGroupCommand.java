package com.collabrium.groups.management.domain.model.commands;

public record RemoveMemberFromMyGroupCommand(
    Long userId,
    Long memberId
) {
}