package com.collabrium.groups.management.domain.model.commands;

public record UpdateGroupCommand(
    Long userId,
    String name,
    String description,
    String imgUrl
) {
}