package com.collabrium.groups.management.domain.model.commands;

public record CreateGroupCommand(
    String name,
    String imgUrl,
    String description,
    Long userId
) {
}