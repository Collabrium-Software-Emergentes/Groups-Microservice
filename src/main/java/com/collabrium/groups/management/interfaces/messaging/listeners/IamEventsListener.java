package com.collabrium.groups.management.interfaces.messaging.listeners;

import com.collabrium.groups.management.domain.model.events.UserLeaderCreatedEvent;
import com.collabrium.groups.management.domain.services.LeaderCommandService;
import com.collabrium.groups.management.interfaces.messaging.transform.CreateLeaderCommandFromEventAssembler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import static com.collabrium.groups.shared.infrastructure.configuration.rabbitmq.RabbitMQConfiguration.USER_LEADER_CREATED_QUEUE;

@Component
public class IamEventsListener {

  private final LeaderCommandService leaderCommandService;

  public IamEventsListener(
      LeaderCommandService leaderCommandService
  ) {

    this.leaderCommandService = leaderCommandService;
  }

  @RabbitListener(queues = USER_LEADER_CREATED_QUEUE)
  public void handle(UserLeaderCreatedEvent event) {

    var createLeaderCommand = CreateLeaderCommandFromEventAssembler.toCommandFromEvent(event);

    leaderCommandService.handle(createLeaderCommand);
  }
}
