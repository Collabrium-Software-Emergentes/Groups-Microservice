package com.collabrium.groups.management.application.internal.queryservices;

import com.collabrium.groups.management.domain.model.aggregates.Leader;
import com.collabrium.groups.management.domain.model.queries.GetLeaderByIdQuery;
import com.collabrium.groups.management.infrastructure.persistence.jpa.repositories.LeaderRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class LeaderQueryServiceImplTest {

  @Mock
  LeaderRepository leaderRepository;

  @InjectMocks
  LeaderQueryServiceImpl service;

  @Test
  @DisplayName("handle(GetLeaderByIdQuery) - existing leader: returns leader")
  void handle_getLeaderById_existingLeader_returnsLeader() {
    // Arrange
    var leader = mock(Leader.class);
    var query = new GetLeaderByIdQuery(1L);

    when(leaderRepository.findById(1L))
      .thenReturn(Optional.of(leader));

    // Act
    var result = service.handle(query);

    // Assert
    assertThat(result).containsSame(leader);
  }

  @Test
  @DisplayName("handle(GetLeaderByIdQuery) - leader not found: returns empty")
  void handle_getLeaderById_leaderNotFound_returnsEmpty() {
    // Arrange
    var query = new GetLeaderByIdQuery(1L);

    when(leaderRepository.findById(1L))
      .thenReturn(Optional.empty());

    // Act
    var result = service.handle(query);

    // Assert
    assertThat(result).isEmpty();
  }
}