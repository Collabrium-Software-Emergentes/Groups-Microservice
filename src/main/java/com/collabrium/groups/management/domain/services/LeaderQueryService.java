package com.collabrium.groups.management.domain.services;

import com.collabrium.groups.management.domain.model.aggregates.Leader;
import com.collabrium.groups.management.domain.model.queries.GetLeaderByIdQuery;

import java.util.Optional;

public interface LeaderQueryService {

  Optional<Leader> handle(GetLeaderByIdQuery query);
}