package com.collabrium.groups.management.interfaces.rest.resources;

public record InvitationMemberResource(
    Long id,
    String username,
    String name,
    String surname,
    String imgUrl
) {
}