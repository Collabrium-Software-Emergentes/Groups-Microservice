package com.collabrium.groups.management.application.internal.commandservices;

import com.collabrium.groups.management.application.internal.outboundservices.messaging.GroupsEventPublisher;
import com.collabrium.groups.management.application.internal.outboundservices.ports.IamQueryPort;
import com.collabrium.groups.management.domain.exceptions.*;
import com.collabrium.groups.management.domain.model.aggregates.Invitation;
import com.collabrium.groups.management.domain.model.commands.CancelInvitationCommand;
import com.collabrium.groups.management.domain.model.commands.ProcessInvitationCommand;
import com.collabrium.groups.management.domain.model.events.InvitationAcceptedEvent;
import com.collabrium.groups.management.domain.model.valueobjects.MemberId;
import com.collabrium.groups.management.domain.services.InvitationCommandService;
import com.collabrium.groups.management.infrastructure.persistence.jpa.repositories.GroupRepository;
import com.collabrium.groups.management.infrastructure.persistence.jpa.repositories.InvitationRepository;
import org.springframework.stereotype.Service;

@Service
public class InvitationCommandServiceImpl implements InvitationCommandService {

  private final InvitationRepository invitationRepository;
  private final GroupRepository groupRepository;
  private final GroupsEventPublisher groupsEventPublisher;
  private final IamQueryPort iamQueryPort;

  public InvitationCommandServiceImpl(
      InvitationRepository invitationRepository,
      GroupRepository groupRepository,
      GroupsEventPublisher groupsEventPublisher,
      IamQueryPort iamQueryPort
  ) {

    this.invitationRepository = invitationRepository;
    this.groupRepository = groupRepository;
    this.groupsEventPublisher = groupsEventPublisher;
    this.iamQueryPort = iamQueryPort;
  }

  @Override
  public void handle(CancelInvitationCommand command) {

    var user = iamQueryPort.getUserOnlyById(command.userId());

    if (user == null) {
      throw UserNotFoundException.forId(command.userId());
    }

    if (user.memberId() == null) {
      throw MemberNotFoundException.forUserId(command.userId());
    }

    MemberId memberId = MemberId.of(user.memberId());

    var invitation = invitationRepository
        .findByMemberId(memberId)
        .orElseThrow(() ->
            InvitationNotFoundException.forMember(memberId)
        );

    invitationRepository.delete(invitation);
  }

  @Override
  public void handle(ProcessInvitationCommand command) {

    var invitation = validateLeaderInvitationAccess(
        command.userId(),
        command.invitationId()
    );

    if (command.accept()) {
      acceptInvitation(invitation);
      return;
    }

    rejectInvitation(invitation);
  }

  private Invitation validateLeaderInvitationAccess(
      Long userId,
      Long invitationId
  ) {

    var user = iamQueryPort.getUserOnlyById(userId);

    if (user == null) {
      throw UserNotFoundException.forId(userId);
    }

    if (user.leaderId() == null) {
      throw new LeaderNotFoundException(userId);
    }

    var invitation = invitationRepository
        .findDetailedById(invitationId)
        .orElseThrow(() ->
            InvitationNotFoundException.forId(invitationId)
        );

    var leader = invitation
        .getGroup()
        .getLeader();

    if (leader == null || !leader.getId().equals(user.leaderId())) {

      throw new InvalidInvitationAccessException(
          userId,
          invitationId
      );
    }

    return invitation;
  }

  private void acceptInvitation(
      Invitation invitation
  ) {

    var group = invitation.getGroup();

    var memberId =
        invitation.getMemberId();

    group.increaseMemberCount();

    groupRepository.save(group);

    groupsEventPublisher.publishInvitationAccepted(
        new InvitationAcceptedEvent(
            group.getId(),
            memberId.value()
        )
    );

    invitationRepository.delete(invitation);
  }

  private void rejectInvitation(
      Invitation invitation
  ) {

    invitationRepository.delete(invitation);
  }
}
