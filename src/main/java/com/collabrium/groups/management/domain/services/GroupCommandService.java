package com.collabrium.groups.management.domain.services;

import com.collabrium.groups.management.domain.model.aggregates.Group;
import com.collabrium.groups.management.domain.model.commands.CreateGroupCommand;
import com.collabrium.groups.management.domain.model.commands.DeleteGroupCommand;
import com.collabrium.groups.management.domain.model.commands.LeaveGroupCommand;
import com.collabrium.groups.management.domain.model.commands.RemoveMemberFromMyGroupCommand;
import com.collabrium.groups.management.domain.model.commands.UpdateGroupCommand;

import java.util.Optional;

public interface GroupCommandService {

  Optional<Group> handle(CreateGroupCommand command);

  Optional<Group> handle(UpdateGroupCommand command);

  void handle(DeleteGroupCommand command);

  void handle(RemoveMemberFromMyGroupCommand command);

  void handle(LeaveGroupCommand command);
}