package com.collabrium.groups.management.interfaces.rest.transform;

import com.collabrium.groups.management.application.internal.dto.LeaderDetailsDTO;
import com.collabrium.groups.management.interfaces.rest.resources.LeaderDetailsResource;

public class LeaderDetailsResourceFromDTOAssembler {

  private LeaderDetailsResourceFromDTOAssembler() {
  }

  public static LeaderDetailsResource toResourceFromDTO(
      LeaderDetailsDTO dto
  ){

    return new LeaderDetailsResource(
        dto.leaderId(),
        dto.username(),
        dto.name(),
        dto.surname(),
        dto.imgUrl(),
        dto.email(),
        dto.averageSolutionTime(),
        dto.solvedRequests()
    );
  }
}