package com.collabrium.groups.management.interfaces.messaging.transform;

import com.collabrium.groups.management.domain.model.commands.LeaveGroupCommand;
import com.collabrium.groups.management.domain.model.events.MemberLeftEvent;

public class LeaveGroupCommandFromEventAssembler {

  private LeaveGroupCommandFromEventAssembler() {
  }

  public static LeaveGroupCommand toCommandFromEvent(
      MemberLeftEvent event
  ) {

    return new LeaveGroupCommand(
        event.groupId()
    );
  }
}