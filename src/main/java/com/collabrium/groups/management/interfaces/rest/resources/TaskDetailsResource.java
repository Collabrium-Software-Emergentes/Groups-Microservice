package com.collabrium.groups.management.interfaces.rest.resources;

public record TaskDetailsResource(
    Long id,
    String title,
    String description,
    String dueDate,
    String createdAt,
    String updatedAt,
    String status,
    TaskMemberDetailsResource member,
    Long groupId
) {
}