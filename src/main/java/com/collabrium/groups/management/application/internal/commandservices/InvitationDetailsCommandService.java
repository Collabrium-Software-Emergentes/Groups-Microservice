package com.collabrium.groups.management.application.internal.commandservices;

import com.collabrium.groups.management.application.internal.dto.InvitationDetailsDTO;
import com.collabrium.groups.management.application.internal.outboundservices.ports.IamQueryPort;
import com.collabrium.groups.management.domain.exceptions.GroupNotFoundException;
import com.collabrium.groups.management.domain.exceptions.InvitationAlreadyExistsException;
import com.collabrium.groups.management.domain.exceptions.MemberNotFoundException;
import com.collabrium.groups.management.domain.exceptions.UserNotFoundException;
import com.collabrium.groups.management.domain.model.aggregates.Invitation;
import com.collabrium.groups.management.domain.model.commands.CreateInvitationCommand;
import com.collabrium.groups.management.domain.model.valueobjects.MemberId;
import com.collabrium.groups.management.infrastructure.persistence.jpa.repositories.GroupRepository;
import com.collabrium.groups.management.infrastructure.persistence.jpa.repositories.InvitationRepository;
import com.collabrium.groups.shared.infrastructure.clients.iam.resources.UserOnlyResource;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class InvitationDetailsCommandService {

  private final InvitationRepository invitationRepository;
  private final GroupRepository groupRepository;
  private final IamQueryPort iamQueryPort;

  public InvitationDetailsCommandService(
      InvitationRepository invitationRepository,
      GroupRepository groupRepository,
      IamQueryPort iamQueryPort
  ) {

    this.invitationRepository = invitationRepository;
    this.groupRepository = groupRepository;
    this.iamQueryPort = iamQueryPort;
  }

  public Optional<InvitationDetailsDTO> handle(CreateInvitationCommand command) {

    var group = groupRepository
        .findById(command.groupId())
        .orElseThrow(() ->
            GroupNotFoundException.forId(command.groupId())
        );

    var user = iamQueryPort.getUserOnlyById(command.userId());

    validateUser(command.userId(), user);

    MemberId memberId = MemberId.of(user.memberId());

    if (invitationRepository.existsByMemberId(memberId)) {

      throw new InvitationAlreadyExistsException(
          memberId
      );
    }

    var invitation = new Invitation(
        memberId,
        group
    );

    var savedInvitation = invitationRepository.save(invitation);

    return Optional.of(
        buildInvitationDetailsDTO(
            savedInvitation,
            user,
            command.userId()
        )
    );
  }

  private void validateUser(
      Long userId,
      UserOnlyResource user
  ) {

    if (user == null) {
      throw UserNotFoundException.forId(userId);
    }

    if (user.memberId() == null) {
      throw MemberNotFoundException.forUserId(userId);
    }
  }

  private InvitationDetailsDTO buildInvitationDetailsDTO(
      Invitation invitation,
      UserOnlyResource user,
      Long userId
  ) {

    var group = invitation.getGroup();

    return new InvitationDetailsDTO(
        invitation.getId(),
        userId,
        user.username(),
        user.name(),
        user.surname(),
        user.imgUrl(),
        group.getId(),
        group.getName(),
        group.getImgUrl() != null
            ? group.getImgUrl().imgUrl()
            : null,
        group.getDescription(),
        group.getCode().value(),
        group.getMemberCount()
    );
  }
}