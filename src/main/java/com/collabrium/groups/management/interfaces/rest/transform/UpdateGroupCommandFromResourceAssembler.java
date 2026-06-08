package com.collabrium.groups.management.interfaces.rest.transform;

import com.collabrium.groups.management.domain.model.commands.UpdateGroupCommand;
import com.collabrium.groups.management.interfaces.rest.resources.UpdateGroupResource;

public class UpdateGroupCommandFromResourceAssembler {

  private UpdateGroupCommandFromResourceAssembler() {
  }

  public static UpdateGroupCommand toCommandFromResource(
      Long userId,
      UpdateGroupResource resource
  ) {

    return new UpdateGroupCommand(
        userId,
        resource.name(),
        resource.description(),
        resource.imgUrl()
    );
  }
}