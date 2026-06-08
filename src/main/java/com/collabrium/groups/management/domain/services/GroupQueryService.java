package com.collabrium.groups.management.domain.services;

import com.collabrium.groups.management.domain.model.aggregates.Group;
import com.collabrium.groups.management.domain.model.queries.*;

import java.util.Optional;

public interface GroupQueryService {

  Optional<Group> handle(GetGroupByIdQuery query);

  Optional<Group> handle(GetGroupByLeaderIdQuery query);

  Optional<Group> handle(GetGroupByCodeQuery query);

  Optional<Group> handle(GetGroupByUserIdQuery query);
}