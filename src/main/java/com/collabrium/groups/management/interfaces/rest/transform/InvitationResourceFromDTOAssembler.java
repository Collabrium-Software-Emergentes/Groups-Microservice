package com.collabrium.groups.management.interfaces.rest.transform;

import com.collabrium.groups.management.application.internal.dto.InvitationDetailsDTO;
import com.collabrium.groups.management.interfaces.rest.resources.GroupResource;
import com.collabrium.groups.management.interfaces.rest.resources.InvitationMemberResource;
import com.collabrium.groups.management.interfaces.rest.resources.InvitationResource;

public class InvitationResourceFromDTOAssembler {

  private InvitationResourceFromDTOAssembler() {
  }

  public static InvitationResource toResourceFromDTO(
      InvitationDetailsDTO dto
  ) {

    return new InvitationResource(

        dto.id(),

        new InvitationMemberResource(
            dto.userId(),
            dto.username(),
            dto.name(),
            dto.surname(),
            dto.imgUrl()
        ),

        new GroupResource(
            dto.groupId(),
            dto.groupName(),
            dto.groupImgUrl(),
            dto.groupDescription(),
            dto.groupCode(),
            dto.memberCount()
        )
    );
  }
}