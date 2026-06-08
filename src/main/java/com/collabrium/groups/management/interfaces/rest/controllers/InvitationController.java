package com.collabrium.groups.management.interfaces.rest.controllers;

import com.collabrium.groups.management.domain.model.commands.CancelInvitationCommand;
import com.collabrium.groups.management.domain.model.commands.ProcessInvitationCommand;
import com.collabrium.groups.management.domain.services.InvitationCommandService;
import com.collabrium.groups.shared.infrastructure.security.AuthenticatedUser;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1")
@Tag(name = "Invitations", description = "Invitation Management Endpoints")
public class InvitationController {

  private final InvitationCommandService invitationCommandService;

  public InvitationController(
      InvitationCommandService invitationCommandService
  ) {

    this.invitationCommandService = invitationCommandService;
  }

  @DeleteMapping("/invitations/member")
  @Operation(summary = "Cancel an invitation", description = "Cancel an existing invitation by a member")
  public ResponseEntity<Void> cancelInvitation(
      @AuthenticationPrincipal AuthenticatedUser user
  ) {

    var cancelInvitationCommand = new CancelInvitationCommand(user.userId());

    invitationCommandService.handle(cancelInvitationCommand);

    return ResponseEntity.noContent().build();
  }

  @PatchMapping("/group/invitations/{invitationId}")
  @Operation(summary = "Accept or decline an invitation", description = "Accept or decline an invitation for a leader")
  public ResponseEntity<Void> processInvitation(
      @PathVariable Long invitationId,
      @AuthenticationPrincipal AuthenticatedUser user,
      @RequestParam(defaultValue = "false") boolean accept
  ) {

    var processInvitationCommand = new ProcessInvitationCommand(user.userId(), invitationId, accept);

    invitationCommandService.handle(processInvitationCommand);

    return ResponseEntity.noContent().build();
  }
}
