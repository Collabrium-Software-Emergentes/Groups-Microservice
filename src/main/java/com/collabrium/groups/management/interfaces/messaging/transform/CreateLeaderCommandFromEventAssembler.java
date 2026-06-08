package com.collabrium.groups.management.interfaces.messaging.transform;

import com.collabrium.groups.management.domain.model.commands.CreateLeaderCommand;
import com.collabrium.groups.management.domain.model.events.UserLeaderCreatedEvent;

public class CreateLeaderCommandFromEventAssembler {

  private CreateLeaderCommandFromEventAssembler() {
  }

  public static CreateLeaderCommand toCommandFromEvent(
      UserLeaderCreatedEvent event
  ) {

    return new CreateLeaderCommand(
        event.userId()
    );
  }
}
