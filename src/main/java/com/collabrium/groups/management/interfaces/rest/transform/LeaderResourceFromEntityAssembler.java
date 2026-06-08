package com.collabrium.groups.management.interfaces.rest.transform;

import com.collabrium.groups.management.domain.model.aggregates.Leader;
import com.collabrium.groups.management.interfaces.rest.resources.LeaderResource;

public class LeaderResourceFromEntityAssembler {

  private LeaderResourceFromEntityAssembler() {
  }

  public static LeaderResource toResourceFromEntity(
      Leader entity
  ) {

    return new LeaderResource(
      entity.getId(),
      entity.getFormattedAverageSolutionTime(),
      entity.getSolvedRequests()
    );
  }
}
