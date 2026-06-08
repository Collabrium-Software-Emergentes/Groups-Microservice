package com.collabrium.groups.management.domain.services;

import com.collabrium.groups.management.domain.model.commands.CreateLeaderCommand;

public interface LeaderCommandService {

  void handle(CreateLeaderCommand command);
}