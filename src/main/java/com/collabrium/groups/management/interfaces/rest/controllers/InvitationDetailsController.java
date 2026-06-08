package com.collabrium.groups.management.interfaces.rest.controllers;

import com.collabrium.groups.management.application.internal.commandservices.InvitationDetailsCommandService;
import com.collabrium.groups.management.application.internal.queryservices.InvitationDetailsQueryService;
import com.collabrium.groups.management.domain.model.commands.CreateInvitationCommand;
import com.collabrium.groups.management.domain.model.queries.GetInvitationByUserIdQuery;
import com.collabrium.groups.management.domain.model.queries.GetInvitationsOfMyGroupQuery;
import com.collabrium.groups.management.interfaces.rest.resources.InvitationResource;
import com.collabrium.groups.management.interfaces.rest.transform.InvitationResourceFromDTOAssembler;
import com.collabrium.groups.shared.infrastructure.security.AuthenticatedUser;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/invitations")
@Tag(name = "Invitations Creation", description = "Invitation Creation Management Endpoint")
public class InvitationDetailsController {

  private final InvitationDetailsCommandService invitationDetailsCommandService;
  private final InvitationDetailsQueryService invitationDetailsQueryService;

  public InvitationDetailsController(
      InvitationDetailsCommandService invitationDetailsCommandService,
      InvitationDetailsQueryService invitationDetailsQueryService
  ) {

    this.invitationDetailsCommandService = invitationDetailsCommandService;
    this.invitationDetailsQueryService = invitationDetailsQueryService;
  }

  @PostMapping("/groups/{groupId}")
  @Operation(summary = "Create a new invitation", description = "Create a new invitation for a group")
  public ResponseEntity<InvitationResource> createInvitation(
      @PathVariable Long groupId,
      @AuthenticationPrincipal AuthenticatedUser user
  ) {

    var createInvitationCommand = new CreateInvitationCommand(user.userId(), groupId);

    var invitationDetails = invitationDetailsCommandService.handle(createInvitationCommand);

    if (invitationDetails.isEmpty()) {
      return ResponseEntity.badRequest().build();
    }

    var invitationResource = InvitationResourceFromDTOAssembler
        .toResourceFromDTO(invitationDetails.get());

    return ResponseEntity.ok(invitationResource);
  }

  @GetMapping("/group")
  @Operation(
      summary = "Get invitations of my group",
      description = "Returns all invitations belonging to the authenticated leader group"
  )
  public ResponseEntity<List<InvitationResource>> getInvitationsOfMyGroup(
      @AuthenticationPrincipal AuthenticatedUser user
  ) {

    var getInvitationsOfMyGroupQuery = new GetInvitationsOfMyGroupQuery(user.userId());

    var invitations = invitationDetailsQueryService.handle(getInvitationsOfMyGroupQuery);

    var resources = invitations
        .stream()
        .map(InvitationResourceFromDTOAssembler::toResourceFromDTO)
        .toList();

    return ResponseEntity.ok(resources);
  }

  @GetMapping("/member")
  @Operation(
      summary = "Get my invitation",
      description = "Get the current user's invitation"
  )
  public ResponseEntity<InvitationResource> getMyInvitation(
      @AuthenticationPrincipal AuthenticatedUser user
  ) {

    var getInvitationByUserIdQuery = new GetInvitationByUserIdQuery(user.userId());

    var invitationDetails = invitationDetailsQueryService.handle(getInvitationByUserIdQuery);

    if (invitationDetails.isEmpty()) {
      return ResponseEntity.notFound().build();
    }

    var invitationResource = InvitationResourceFromDTOAssembler.toResourceFromDTO(invitationDetails.get());

    return ResponseEntity.ok(invitationResource);
  }
}