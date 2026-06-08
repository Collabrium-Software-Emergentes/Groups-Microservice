package com.collabrium.groups.management.application.internal.commandservices;

import com.collabrium.groups.management.application.internal.outboundservices.messaging.GroupsEventPublisher;
import com.collabrium.groups.management.domain.model.aggregates.Leader;
import com.collabrium.groups.management.domain.model.commands.CreateLeaderCommand;
import com.collabrium.groups.management.domain.model.events.LeaderCreatedEvent;
import com.collabrium.groups.management.domain.services.LeaderCommandService;
import com.collabrium.groups.management.infrastructure.persistence.jpa.repositories.LeaderRepository;
import org.springframework.stereotype.Service;

@Service
public class LeaderCommandServiceImpl implements LeaderCommandService {

  private final LeaderRepository leaderRepository;
  private final GroupsEventPublisher groupsEventPublisher;

  public LeaderCommandServiceImpl(
      LeaderRepository leaderRepository,
      GroupsEventPublisher groupsEventPublisher
  ) {

    this.leaderRepository = leaderRepository;
    this.groupsEventPublisher = groupsEventPublisher;
  }

  @Override
  public void handle(CreateLeaderCommand command) {

    Leader newLeader = new Leader();

    var savedLeader = leaderRepository.save(newLeader);

    var leaderCreatedEvent = new LeaderCreatedEvent(
        command.userId(),
        savedLeader.getId()
    );

    groupsEventPublisher.publishLeaderCreated(leaderCreatedEvent);
  }
}
