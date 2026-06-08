package com.collabrium.groups.management.application.internal.dto;

public record InvitationDetailsDTO(
    Long id,
    Long userId,
    String username,
    String name,
    String surname,
    String imgUrl,
    Long groupId,
    String groupName,
    String groupImgUrl,
    String groupDescription,
    String groupCode,
    Integer memberCount
) {
}