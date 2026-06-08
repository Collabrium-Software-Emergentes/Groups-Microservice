package com.collabrium.groups.management.interfaces.rest.transform;

import com.collabrium.groups.management.domain.model.aggregates.Group;
import com.collabrium.groups.management.interfaces.rest.resources.GroupWithLeaderResource;

public class GroupWithLeaderResourceFromEntityAssembler {

  private GroupWithLeaderResourceFromEntityAssembler() {
  }

  public static GroupWithLeaderResource toResourceFromEntity(
      Group entity
  ) {

    return new GroupWithLeaderResource(
        entity.getId(),
        entity.getName(),
        entity.getImgUrl().imgUrl(),
        entity.getDescription(),
        entity.getCode().value(),
        entity.getMemberCount(),
        entity.getLeader().getId()
    );
  }
}