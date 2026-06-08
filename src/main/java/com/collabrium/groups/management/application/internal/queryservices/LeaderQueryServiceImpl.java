package com.collabrium.groups.management.application.internal.queryservices;

import com.collabrium.groups.management.domain.model.aggregates.Leader;
import com.collabrium.groups.management.domain.model.queries.GetLeaderByIdQuery;
import com.collabrium.groups.management.domain.services.LeaderQueryService;
import com.collabrium.groups.management.infrastructure.persistence.jpa.repositories.LeaderRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class LeaderQueryServiceImpl implements LeaderQueryService {

  private final LeaderRepository leaderRepository;

  public LeaderQueryServiceImpl(
      LeaderRepository leaderRepository
  ) {

    this.leaderRepository = leaderRepository;
  }

  @Override
  public Optional<Leader> handle(GetLeaderByIdQuery query) {
    return leaderRepository.findById(query.leaderId());
  }
}