package com.collabrium.groups.management.interfaces.rest.transform;

import com.collabrium.groups.management.domain.model.commands.CreateGroupCommand;
import com.collabrium.groups.management.interfaces.rest.resources.CreateGroupResource;

public class CreateGroupCommandFromResourceAssembler {

  private CreateGroupCommandFromResourceAssembler() {
  }

  public static CreateGroupCommand toCommandFromResource(
      CreateGroupResource resource,
      Long userId) {

    return new CreateGroupCommand(
        resource.name(),
        resource.imgUrl(),
        resource.description(),
        userId
    );
  }
}