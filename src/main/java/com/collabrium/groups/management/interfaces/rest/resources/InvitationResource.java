package com.collabrium.groups.management.interfaces.rest.resources;

public record InvitationResource(
    Long id,
    InvitationMemberResource member,
    GroupResource group
) {
}