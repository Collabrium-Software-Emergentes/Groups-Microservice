package com.collabrium.groups.management.infrastructure.messaging;

import com.collabrium.groups.management.application.internal.outboundservices.messaging.GroupsEventPublisher;
import com.collabrium.groups.management.domain.model.events.GroupDeletedEvent;
import com.collabrium.groups.management.domain.model.events.InvitationAcceptedEvent;
import com.collabrium.groups.management.domain.model.events.LeaderCreatedEvent;
import com.collabrium.groups.management.domain.model.events.RemoveMemberEvent;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.stereotype.Service;

import static com.collabrium.groups.shared.infrastructure.configuration.rabbitmq.RabbitMQConfiguration.*;

@Service
public class GroupsEventPublisherImpl implements GroupsEventPublisher {

  private final AmqpTemplate rabbitTemplate;

  public GroupsEventPublisherImpl(
      AmqpTemplate rabbitTemplate
  ) {

    this.rabbitTemplate = rabbitTemplate;
  }

  @Override
  public void publishLeaderCreated(LeaderCreatedEvent event) {

    rabbitTemplate.convertAndSend(
        GROUPS_EXCHANGE,
        LEADER_CREATED_KEY,
        event
    );
  }

  @Override
  public void publishInvitationAccepted(InvitationAcceptedEvent event) {

    rabbitTemplate.convertAndSend(
        GROUPS_EXCHANGE,
        INVITATION_ACCEPTED_KEY,
        event
    );
  }

  @Override
  public void publishMemberRemovedFromGroup(RemoveMemberEvent event) {

    rabbitTemplate.convertAndSend(
        GROUPS_EXCHANGE,
        MEMBER_REMOVED_FROM_GROUP_KEY,
        event
    );
  }

  @Override
  public void publishGroupDeleted(GroupDeletedEvent event) {

    rabbitTemplate.convertAndSend(
        GROUPS_EXCHANGE,
        GROUP_DELETED_KEY,
        event
    );
  }
}