package com.collabrium.groups.management.application.internal.queryservices;

import com.collabrium.groups.management.application.internal.outboundservices.ports.IamQueryPort;
import com.collabrium.groups.management.domain.exceptions.InvalidCodeException;
import com.collabrium.groups.management.domain.model.aggregates.Group;
import com.collabrium.groups.management.domain.model.queries.GetGroupByCodeQuery;
import com.collabrium.groups.management.domain.model.queries.GetGroupByIdQuery;
import com.collabrium.groups.management.domain.model.queries.GetGroupByLeaderIdQuery;
import com.collabrium.groups.management.domain.model.queries.GetGroupByUserIdQuery;
import com.collabrium.groups.management.domain.model.valueobjects.GroupCode;
import com.collabrium.groups.management.infrastructure.persistence.jpa.repositories.GroupRepository;
import com.collabrium.groups.shared.infrastructure.clients.iam.resources.UserOnlyResource;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
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

  @Test
  @DisplayName("handle(GetGroupByCodeQuery) - null code: throws InvalidCodeException")
  void handle_getGroupByCode_nullCode_throwsInvalidCodeException() {
    // Arrange
    var query = new GetGroupByCodeQuery(null);

    // Act & Assert
    assertThatThrownBy(() -> service.handle(query))
      .isInstanceOf(InvalidCodeException.class)
      .hasMessage("Group code cannot be null");
  }

  @Test
  @DisplayName("handle(GetGroupByCodeQuery) - invalid code length: throws InvalidCodeException")
  void handle_getGroupByCode_invalidLength_throwsInvalidCodeException() {
    // Arrange
    var query = new GetGroupByCodeQuery("ABC12");

    // Act & Assert
    assertThatThrownBy(() -> service.handle(query))
      .isInstanceOf(InvalidCodeException.class)
      .hasMessage("Group code must be exactly 9 characters. Received: 5 characters");
  }

  @Test
  @DisplayName("handle(GetGroupByCodeQuery) - invalid code format: throws InvalidCodeException")
  void handle_getGroupByCode_invalidFormat_throwsInvalidCodeException() {
    // Arrange
    var query = new GetGroupByCodeQuery("abc-12345");

    // Act & Assert
    assertThatThrownBy(() -> service.handle(query))
      .isInstanceOf(InvalidCodeException.class)
      .hasMessage(
        "Invalid group code format: 'abc-12345'. Expected: 9 alphanumeric characters (0-9, A-Z)"
      );
  }

  @Test
  @DisplayName("handle(GetGroupByUserIdQuery) - user not found: returns empty")
  void handle_getGroupByUserId_userNotFound_returnsEmpty() {
    // Arrange
    var query = new GetGroupByUserIdQuery(1L);

    when(iamQueryPort.getUserOnlyById(1L)).thenReturn(null);

    // Act
    var result = service.handle(query);

    // Assert
    assertThat(result).isEmpty();
  }

  @Test
  @DisplayName("handle(GetGroupByUserIdQuery) - user without leader id: returns empty")
  void handle_getGroupByUserId_userWithoutLeaderId_returnsEmpty() {
    // Arrange
    var query = new GetGroupByUserIdQuery(1L);

    var user = new UserOnlyResource(
      "john",
      "John",
      "Doe",
      null,
      "john@test.com",
      null,
      10L
    );

    when(iamQueryPort.getUserOnlyById(1L)).thenReturn(user);

    // Act
    var result = service.handle(query);

    // Assert
    assertThat(result).isEmpty();
  }

  @Test
  @DisplayName("handle(GetGroupByUserIdQuery) - user with existing leader group: returns group")
  void handle_getGroupByUserId_existingLeaderGroup_returnsGroup() {
    // Arrange
    var group = mock(Group.class);
    var query = new GetGroupByUserIdQuery(1L);

    var user = new UserOnlyResource(
      "john",
      "John",
      "Doe",
      null,
      "john@test.com",
      20L,
      null
    );

    when(iamQueryPort.getUserOnlyById(1L)).thenReturn(user);
    when(groupRepository.findByLeaderId(20L)).thenReturn(Optional.of(group));

    // Act
    var result = service.handle(query);

    // Assert
    assertThat(result).containsSame(group);
  }

  @Test
  @DisplayName("handle(GetGroupByUserIdQuery) - user leader group not found: returns empty")
  void handle_getGroupByUserId_leaderGroupNotFound_returnsEmpty() {
    // Arrange
    var query = new GetGroupByUserIdQuery(1L);

    var user = new UserOnlyResource(
      "john",
      "John",
      "Doe",
      null,
      "john@test.com",
      20L,
      null
    );

    when(iamQueryPort.getUserOnlyById(1L)).thenReturn(user);
    when(groupRepository.findByLeaderId(20L)).thenReturn(Optional.empty());

    // Act
    var result = service.handle(query);

    // Assert
    assertThat(result).isEmpty();
  }
}