package com.collabrium.groups.management.application.internal.dto;

public record TaskDetailsDTO(
    Long id,
    String title,
    String description,
    String dueDate,
    String createdAt,
    String updatedAt,
    String status,
    TaskMemberDetailsDTO member,
    Long groupId
) {
}