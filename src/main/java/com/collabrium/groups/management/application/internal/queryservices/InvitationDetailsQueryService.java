package com.collabrium.groups.management.application.internal.queryservices;

import com.collabrium.groups.management.application.internal.dto.InvitationDetailsDTO;
import com.collabrium.groups.management.application.internal.outboundservices.ports.IamQueryPort;
import com.collabrium.groups.management.domain.exceptions.GroupNotFoundException;
import com.collabrium.groups.management.domain.exceptions.LeaderNotFoundException;
import com.collabrium.groups.management.domain.exceptions.MemberNotFoundException;
import com.collabrium.groups.management.domain.exceptions.UserNotFoundException;
import com.collabrium.groups.management.domain.model.aggregates.Invitation;
import com.collabrium.groups.management.domain.model.queries.GetInvitationByUserIdQuery;
import com.collabrium.groups.management.domain.model.queries.GetInvitationsOfMyGroupQuery;
import com.collabrium.groups.management.domain.model.valueobjects.MemberId;
import com.collabrium.groups.management.infrastructure.persistence.jpa.repositories.GroupRepository;
import com.collabrium.groups.management.infrastructure.persistence.jpa.repositories.InvitationRepository;
import com.collabrium.groups.shared.infrastructure.clients.iam.resources.UserOnlyResource;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class InvitationDetailsQueryService {

  private final InvitationRepository invitationRepository;
  private final GroupRepository groupRepository;
  private final IamQueryPort iamQueryPort;

  public InvitationDetailsQueryService(
      InvitationRepository invitationRepository,
      GroupRepository groupRepository,
      IamQueryPort iamQueryPort
  ) {

    this.invitationRepository = invitationRepository;
    this.groupRepository = groupRepository;
    this.iamQueryPort = iamQueryPort;
  }

  public Optional<InvitationDetailsDTO> handle(GetInvitationByUserIdQuery query){

    var user = iamQueryPort.getUserOnlyById(query.userId());

    validateUser(query.userId(), user);

    return invitationRepository
        .findByMemberId(
            MemberId.of(user.memberId())
        )
        .map(invitation ->
            buildDTO(invitation, user)
        );
  }

  public List<InvitationDetailsDTO> handle(GetInvitationsOfMyGroupQuery query){

    var user = iamQueryPort.getUserOnlyById(query.userId());

    if (user == null) {
      throw UserNotFoundException.forId(query.userId());
    }

    if (user.leaderId() == null) {
      throw new LeaderNotFoundException(query.userId());
    }

    var group =
        groupRepository
            .findByLeaderId(user.leaderId())
            .orElseThrow(() ->
                new GroupNotFoundException(
                    "Leader does not own a group"
                )
            );

    return invitationRepository
        .findByGroupId(group.getId())
        .stream()
        .map(this::buildDTO)
        .toList();
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

  private InvitationDetailsDTO buildDTO(
      Invitation invitation
  ) {

    Long memberId =
        invitation.getMemberId().value();

    UserOnlyResource user =
        iamQueryPort.getUserByMemberId(memberId);

    return buildDTO(invitation, user);
  }

  private InvitationDetailsDTO buildDTO(
      Invitation invitation,
      UserOnlyResource user
  ) {

    var group = invitation.getGroup();

    return new InvitationDetailsDTO(
        invitation.getId(),
        user != null ? user.memberId() : null,
        user != null ? user.username() : null,
        user != null ? user.name() : null,
        user != null ? user.surname() : null,
        user != null ? user.imgUrl() : null,
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