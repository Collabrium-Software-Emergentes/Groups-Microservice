package com.collabrium.groups.management.application.internal.outboundservices.messaging;

import com.collabrium.groups.management.domain.model.events.GroupDeletedEvent;
import com.collabrium.groups.management.domain.model.events.InvitationAcceptedEvent;
import com.collabrium.groups.management.domain.model.events.LeaderCreatedEvent;
import com.collabrium.groups.management.domain.model.events.RemoveMemberEvent;

public interface GroupsEventPublisher {

  void publishLeaderCreated(LeaderCreatedEvent event);

  void publishInvitationAccepted(InvitationAcceptedEvent event);

  void publishMemberRemovedFromGroup(RemoveMemberEvent event);

  void publishGroupDeleted(GroupDeletedEvent event);
}