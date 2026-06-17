package com.collabrium.groups.management.application.internal.queryservices;

import com.collabrium.groups.management.application.internal.outboundservices.ports.IamQueryPort;
import com.collabrium.groups.management.domain.model.aggregates.Group;
import com.collabrium.groups.management.domain.model.queries.GetGroupByCodeQuery;
import com.collabrium.groups.management.domain.model.queries.GetGroupByIdQuery;
import com.collabrium.groups.management.domain.model.queries.GetGroupByLeaderIdQuery;
import com.collabrium.groups.management.domain.model.valueobjects.GroupCode;
import com.collabrium.groups.management.infrastructure.persistence.jpa.repositories.GroupRepository;
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
public class GroupQueryServiceImplTest {

  @Mock
  GroupRepository groupRepository;

  @Mock
  IamQueryPort iamQueryPort;

  @InjectMocks
  GroupQueryServiceImpl service;

  @Test
  @DisplayName("handle(GetGroupByIdQuery) - existing group: returns group")
  void handle_getGroupById_existingGroup_returnsGroup() {
    // Arrange
    var group = mock(Group.class);
    var query = new GetGroupByIdQuery(1L);

    when(groupRepository.findById(1L)).thenReturn(Optional.of(group));

    // Act
    var result = service.handle(query);

    // Assert
    assertThat(result).containsSame(group);
  }

  @Test
  @DisplayName("handle(GetGroupByIdQuery) - group not found: returns empty")
  void handle_getGroupById_groupNotFound_returnsEmpty() {
    // Arrange
    var query = new GetGroupByIdQuery(1L);

    when(groupRepository.findById(1L)).thenReturn(Optional.empty());

    // Act
    var result = service.handle(query);

    // Assert
    assertThat(result).isEmpty();
  }

  @Test
  @DisplayName("handle(GetGroupByLeaderIdQuery) - existing group: returns group")
  void handle_getGroupByLeaderId_existingGroup_returnsGroup() {
    // Arrange
    var group = mock(Group.class);
    var query = new GetGroupByLeaderIdQuery(10L);

    when(groupRepository.findByLeaderId(10L)).thenReturn(Optional.of(group));

    // Act
    var result = service.handle(query);

    // Assert
    assertThat(result).containsSame(group);
  }

  @Test
  @DisplayName("handle(GetGroupByLeaderIdQuery) - group not found: returns empty")
  void handle_getGroupByLeaderId_groupNotFound_returnsEmpty() {
    // Arrange
    var query = new GetGroupByLeaderIdQuery(10L);

    when(groupRepository.findByLeaderId(10L)).thenReturn(Optional.empty());

    // Act
    var result = service.handle(query);

    // Assert
    assertThat(result).isEmpty();
  }

  @Test
  @DisplayName("handle(GetGroupByCodeQuery) - valid code and existing group: returns group")
  void handle_getGroupByCode_validCode_existingGroup_returnsGroup() {
    // Arrange
    var group = mock(Group.class);
    var query = new GetGroupByCodeQuery("ABC123456");
    var code = GroupCode.fromString("ABC123456");

    when(groupRepository.findByCode(code)).thenReturn(Optional.of(group));

    // Act
    var result = service.handle(query);

    // Assert
    assertThat(result).containsSame(group);
  }

  @Test
  @DisplayName("handle(GetGroupByCodeQuery) - valid code and no group found: returns empty")
  void handle_getGroupByCode_validCode_noGroupFound_returnsEmpty() {
    // Arrange
    var query = new GetGroupByCodeQuery("ABC123456");
    var code = GroupCode.fromString("ABC123456");

    when(groupRepository.findByCode(code)).thenReturn(Optional.empty());

    // Act
    var result = service.handle(query);

    // Assert
    assertThat(result).isEmpty();
  }
}