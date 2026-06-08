package com.collabrium.groups.management.interfaces.messaging.listeners;

import com.collabrium.groups.management.domain.model.events.MemberLeftEvent;
import com.collabrium.groups.management.domain.services.GroupCommandService;
import com.collabrium.groups.management.interfaces.messaging.transform.LeaveGroupCommandFromEventAssembler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import static com.collabrium.groups.shared.infrastructure.configuration.rabbitmq.RabbitMQConfiguration.MEMBER_LEFT_GROUP_QUEUE;

@Component
public class TasksEventsListener {

  private final GroupCommandService groupCommandService;

  public TasksEventsListener(
      GroupCommandService groupCommandService
  ) {

    this.groupCommandService = groupCommandService;
  }

  @RabbitListener(queues = MEMBER_LEFT_GROUP_QUEUE)
  public void handle(MemberLeftEvent event) {

    var leaveGroupCommand = LeaveGroupCommandFromEventAssembler.toCommandFromEvent(event);

    groupCommandService.handle(leaveGroupCommand);
  }
}