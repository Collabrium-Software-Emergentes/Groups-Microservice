package com.collabrium.groups.management.interfaces.rest.transform;

import com.collabrium.groups.management.application.internal.dto.TaskDetailsDTO;
import com.collabrium.groups.management.interfaces.rest.resources.TaskDetailsResource;
import com.collabrium.groups.management.interfaces.rest.resources.TaskMemberDetailsResource;

public class TaskDetailsResourceFromDTOAssembler {

  private TaskDetailsResourceFromDTOAssembler() {
  }

  public static TaskDetailsResource toResourceFromDTO (
      TaskDetailsDTO dto
  ) {

    return new TaskDetailsResource(
        dto.id(),
        dto.title(),
        dto.description(),
        dto.dueDate(),
        dto.createdAt(),
        dto.updatedAt(),
        dto.status(),
        new TaskMemberDetailsResource(
            dto.member().id(),
            dto.member().name(),
            dto.member().surname(),
            dto.member().urlImage()
        ),
        dto.groupId()
    );
  }
}