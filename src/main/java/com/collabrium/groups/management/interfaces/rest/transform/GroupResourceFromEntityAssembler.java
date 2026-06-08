package com.collabrium.groups.management.interfaces.rest.transform;

import com.collabrium.groups.management.domain.model.aggregates.Group;
import com.collabrium.groups.management.interfaces.rest.resources.GroupResource;

public class GroupResourceFromEntityAssembler {

  private GroupResourceFromEntityAssembler() {
  }

  public static GroupResource toResourceFromEntity(
      Group entity
  ) {

    return new GroupResource(
        entity.getId(),
        entity.getName(),
        entity.getImgUrl().toString(),
        entity.getDescription(),
        entity.getCode().toString(),
        entity.getMemberCount()
    );
  }
}