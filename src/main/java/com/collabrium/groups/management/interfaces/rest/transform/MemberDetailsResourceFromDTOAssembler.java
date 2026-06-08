package com.collabrium.groups.management.interfaces.rest.transform;

import com.collabrium.groups.management.application.internal.dto.MemberDetailsDTO;
import com.collabrium.groups.management.interfaces.rest.resources.MembersDetailsResource;

public class MemberDetailsResourceFromDTOAssembler {

  private MemberDetailsResourceFromDTOAssembler() {
  }

  public static MembersDetailsResource toResourceFromDTO(
      MemberDetailsDTO dto
  ) {

    return new MembersDetailsResource(
        dto.id(),
        dto.username(),
        dto.name(),
        dto.surname(),
        dto.imgUrl()
    );
  }
}