package com.collabrium.groups.management.domain.services;

import com.collabrium.groups.management.domain.model.commands.CancelInvitationCommand;
import com.collabrium.groups.management.domain.model.commands.ProcessInvitationCommand;

public interface InvitationCommandService {

  void handle(CancelInvitationCommand command);

  void handle(ProcessInvitationCommand command);
}