package com.collabrium.groups.management.application.internal.queryservices;

import com.collabrium.groups.management.application.internal.outboundservices.ports.IamQueryPort;
import com.collabrium.groups.management.domain.exceptions.GroupNotFoundException;
import com.collabrium.groups.management.domain.exceptions.LeaderNotFoundException;
import com.collabrium.groups.management.domain.exceptions.MemberNotFoundException;
import com.collabrium.groups.management.domain.exceptions.UserNotFoundException;
import com.collabrium.groups.management.domain.model.aggregates.Group;
import com.collabrium.groups.management.domain.model.aggregates.Invitation;
import com.collabrium.groups.management.domain.model.queries.GetInvitationByUserIdQuery;
import com.collabrium.groups.management.domain.model.queries.GetInvitationsOfMyGroupQuery;
import com.collabrium.groups.management.domain.model.valueobjects.GroupCode;
import com.collabrium.groups.management.domain.model.valueobjects.MemberId;
import com.collabrium.groups.management.infrastructure.persistence.jpa.repositories.GroupRepository;
import com.collabrium.groups.management.infrastructure.persistence.jpa.repositories.InvitationRepository;
import com.collabrium.groups.shared.infrastructure.clients.iam.resources.UserOnlyResource;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class InvitationDetailsQueryServiceTest {

  @Mock
  InvitationRepository invitationRepository;

  @Mock
  GroupRepository groupRepository;

  @Mock
  IamQueryPort iamQueryPort;

  @InjectMocks
  InvitationDetailsQueryService service;

  @Test
  @DisplayName("handle(GetInvitationByUserIdQuery) - user not found: throws UserNotFoundException")
  void handle_getInvitationByUserId_userNotFound_throwsUserNotFoundException() {

    // Arrange
    var query = new GetInvitationByUserIdQuery(1L);

    when(iamQueryPort.getUserOnlyById(1L))
      .thenReturn(null);

    // Act & Assert
    assertThatThrownBy(() -> service.handle(query))
      .isInstanceOf(UserNotFoundException.class)
      .hasMessage("User with id 1 was not found");
  }

  @Test
  @DisplayName("handle(GetInvitationByUserIdQuery) - user without member id: throws MemberNotFoundException")
  void handle_getInvitationByUserId_userWithoutMemberId_throwsMemberNotFoundException() {

    // Arrange
    var query = new GetInvitationByUserIdQuery(1L);

    var user = new UserOnlyResource(
      "john",
      "John",
      "Doe",
      null,
      "john@test.com",
      10L,
      null
    );

    when(iamQueryPort.getUserOnlyById(1L))
      .thenReturn(user);

    // Act & Assert
    assertThatThrownBy(() -> service.handle(query))
      .isInstanceOf(MemberNotFoundException.class)
      .hasMessage("No member associated with user id 1 was found");
  }

  @Test
  @DisplayName("handle(GetInvitationByUserIdQuery) - user has no invitation: returns empty")
  void handle_getInvitationByUserId_noInvitation_returnsEmpty() {

    // Arrange
    var query = new GetInvitationByUserIdQuery(1L);

    var user = new UserOnlyResource(
      "john",
      "John",
      "Doe",
      null,
      "john@test.com",
      null,
      20L
    );

    when(iamQueryPort.getUserOnlyById(1L))
      .thenReturn(user);

    when(invitationRepository.findByMemberId(MemberId.of(20L)))
      .thenReturn(Optional.empty());

    // Act
    var result = service.handle(query);

    // Assert
    assertThat(result).isEmpty();
  }

  @Test
  @DisplayName("handle(GetInvitationByUserIdQuery) - existing invitation: returns invitation details")
  void handle_getInvitationByUserId_existingInvitation_returnsInvitationDetails() {

    // Arrange
    var query = new GetInvitationByUserIdQuery(1L);

    var user = new UserOnlyResource(
      "john123",
      "John",
      "Doe",
      "user-img.jpg",
      "john@test.com",
      10L,
      20L
    );

    var invitation = mock(Invitation.class);
    var group = mock(Group.class);

    when(iamQueryPort.getUserOnlyById(1L))
      .thenReturn(user);

    when(invitationRepository.findByMemberId(MemberId.of(20L)))
      .thenReturn(Optional.of(invitation));

    when(invitation.getId())
      .thenReturn(100L);

    when(invitation.getGroup())
      .thenReturn(group);

    when(group.getId())
      .thenReturn(50L);

    when(group.getName())
      .thenReturn("Backend Team");

    when(group.getImgUrl())
      .thenReturn(null);

    when(group.getDescription())
      .thenReturn("Spring Boot developers");

    when(group.getCode())
      .thenReturn(GroupCode.fromString("ABC123456"));

    when(group.getMemberCount())
      .thenReturn(5);

    // Act
    var result = service.handle(query);

    // Assert
    assertThat(result).isPresent();

    var dto = result.get();

    assertThat(dto.id()).isEqualTo(100L);
    assertThat(dto.userId()).isEqualTo(20L);
    assertThat(dto.username()).isEqualTo("john123");
    assertThat(dto.name()).isEqualTo("John");
    assertThat(dto.surname()).isEqualTo("Doe");
    assertThat(dto.imgUrl()).isEqualTo("user-img.jpg");

    assertThat(dto.groupId()).isEqualTo(50L);
    assertThat(dto.groupName()).isEqualTo("Backend Team");
    assertThat(dto.groupImgUrl()).isNull();
    assertThat(dto.groupDescription()).isEqualTo("Spring Boot developers");
    assertThat(dto.groupCode()).isEqualTo("ABC123456");
    assertThat(dto.memberCount()).isEqualTo(5);
  }

  @Test
  @DisplayName("handle(GetInvitationsOfMyGroupQuery) - user not found: throws UserNotFoundException")
  void handle_getInvitationsOfMyGroup_userNotFound_throwsUserNotFoundException() {

    // Arrange
    var query = new GetInvitationsOfMyGroupQuery(1L);

    when(iamQueryPort.getUserOnlyById(1L))
      .thenReturn(null);

    // Act & Assert
    assertThatThrownBy(() -> service.handle(query))
      .isInstanceOf(UserNotFoundException.class)
      .hasMessage("User with id 1 was not found");
  }


  @Test
  @DisplayName("handle(GetInvitationsOfMyGroupQuery) - user without leader id: throws LeaderNotFoundException")
  void handle_getInvitationsOfMyGroup_userWithoutLeaderId_throwsLeaderNotFoundException() {

    // Arrange
    var query = new GetInvitationsOfMyGroupQuery(1L);

    var user = new UserOnlyResource(
      "john",
      "John",
      "Doe",
      null,
      "john@test.com",
      null,
      10L
    );

    when(iamQueryPort.getUserOnlyById(1L))
      .thenReturn(user);

    // Act & Assert
    assertThatThrownBy(() -> service.handle(query))
      .isInstanceOf(LeaderNotFoundException.class)
      .hasMessage("Leader with id 1 was not found");
  }


  @Test
  @DisplayName("handle(GetInvitationsOfMyGroupQuery) - leader without group: throws GroupNotFoundException")
  void handle_getInvitationsOfMyGroup_leaderWithoutGroup_throwsGroupNotFoundException() {

    // Arrange
    var query = new GetInvitationsOfMyGroupQuery(1L);

    var user = new UserOnlyResource(
      "john",
      "John",
      "Doe",
      null,
      "john@test.com",
      20L,
      10L
    );

    when(iamQueryPort.getUserOnlyById(1L))
      .thenReturn(user);

    when(groupRepository.findByLeaderId(20L))
      .thenReturn(Optional.empty());

    // Act & Assert
    assertThatThrownBy(() -> service.handle(query))
      .isInstanceOf(GroupNotFoundException.class)
      .hasMessage("Leader does not own a group");
  }

  @Test
  @DisplayName("handle(GetInvitationsOfMyGroupQuery) - group without invitations: returns empty list")
  void handle_getInvitationsOfMyGroup_groupWithoutInvitations_returnsEmptyList() {

    // Arrange
    var query = new GetInvitationsOfMyGroupQuery(1L);

    var user = new UserOnlyResource(
      "john",
      "John",
      "Doe",
      null,
      "john@test.com",
      20L,
      10L
    );

    var group = mock(Group.class);

    when(iamQueryPort.getUserOnlyById(1L))
      .thenReturn(user);

    when(groupRepository.findByLeaderId(20L))
      .thenReturn(Optional.of(group));

    when(group.getId())
      .thenReturn(50L);

    when(invitationRepository.findByGroupId(50L))
      .thenReturn(List.of());

    // Act
    var result = service.handle(query);

    // Assert
    assertThat(result).isEmpty();
  }


  @Test
  @DisplayName("handle(GetInvitationsOfMyGroupQuery) - invitations with existing users: returns invitation details list")
  void handle_getInvitationsOfMyGroup_existingUsers_returnsInvitationDetailsList() {

    // Arrange
    var query = new GetInvitationsOfMyGroupQuery(1L);

    var owner = new UserOnlyResource(
      "leader",
      "Leader",
      "Doe",
      null,
      "leader@test.com",
      20L,
      10L
    );

    var member = new UserOnlyResource(
      "member01",
      "Alice",
      "Smith",
      "member-img.jpg",
      "member@test.com",
      null,
      30L
    );

    var group = mock(Group.class);
    var invitation = mock(Invitation.class);

    when(iamQueryPort.getUserOnlyById(1L))
      .thenReturn(owner);

    when(groupRepository.findByLeaderId(20L))
      .thenReturn(Optional.of(group));

    when(group.getId())
      .thenReturn(50L);

    when(invitationRepository.findByGroupId(50L))
      .thenReturn(List.of(invitation));

    when(invitation.getId())
      .thenReturn(100L);

    when(invitation.getMemberId())
      .thenReturn(MemberId.of(30L));

    when(invitation.getGroup())
      .thenReturn(group);

    when(iamQueryPort.getUserByMemberId(30L))
      .thenReturn(member);

    when(group.getName())
      .thenReturn("Backend Team");

    when(group.getImgUrl())
      .thenReturn(null);

    when(group.getDescription())
      .thenReturn("Spring Boot developers");

    when(group.getCode())
      .thenReturn(GroupCode.fromString("ABC123456"));

    when(group.getMemberCount())
      .thenReturn(5);

    // Act
    var result = service.handle(query);

    // Assert
    assertThat(result).hasSize(1);

    var dto = result.getFirst();

    assertThat(dto.id()).isEqualTo(100L);
    assertThat(dto.userId()).isEqualTo(30L);
    assertThat(dto.username()).isEqualTo("member01");
    assertThat(dto.name()).isEqualTo("Alice");
    assertThat(dto.surname()).isEqualTo("Smith");
    assertThat(dto.imgUrl()).isEqualTo("member-img.jpg");

    assertThat(dto.groupId()).isEqualTo(50L);
    assertThat(dto.groupName()).isEqualTo("Backend Team");
    assertThat(dto.groupImgUrl()).isNull();
    assertThat(dto.groupDescription()).isEqualTo("Spring Boot developers");
    assertThat(dto.groupCode()).isEqualTo("ABC123456");
    assertThat(dto.memberCount()).isEqualTo(5);
  }

  @Test
  @DisplayName("handle(GetInvitationsOfMyGroupQuery) - invitation without associated user: returns details with null user data")
  void handle_getInvitationsOfMyGroup_userNotAssociated_returnsInvitationDetailsWithNullUserData() {

    // Arrange
    var query = new GetInvitationsOfMyGroupQuery(1L);

    var owner = new UserOnlyResource(
      "leader",
      "Leader",
      "Doe",
      null,
      "leader@test.com",
      20L,
      10L
    );

    var group = mock(Group.class);
    var invitation = mock(Invitation.class);

    when(iamQueryPort.getUserOnlyById(1L))
      .thenReturn(owner);

    when(groupRepository.findByLeaderId(20L))
      .thenReturn(Optional.of(group));

    when(group.getId())
      .thenReturn(50L);

    when(invitationRepository.findByGroupId(50L))
      .thenReturn(List.of(invitation));

    when(invitation.getId())
      .thenReturn(100L);

    when(invitation.getMemberId())
      .thenReturn(MemberId.of(30L));

    when(invitation.getGroup())
      .thenReturn(group);

    when(iamQueryPort.getUserByMemberId(30L))
      .thenReturn(null);

    when(group.getName())
      .thenReturn("Backend Team");

    when(group.getImgUrl())
      .thenReturn(null);

    when(group.getDescription())
      .thenReturn("Spring Boot developers");

    when(group.getCode())
      .thenReturn(GroupCode.fromString("ABC123456"));

    when(group.getMemberCount())
      .thenReturn(5);

    // Act
    var result = service.handle(query);

    // Assert
    assertThat(result).hasSize(1);

    var dto = result.getFirst();

    assertThat(dto.id()).isEqualTo(100L);

    assertThat(dto.userId()).isNull();
    assertThat(dto.username()).isNull();
    assertThat(dto.name()).isNull();
    assertThat(dto.surname()).isNull();
    assertThat(dto.imgUrl()).isNull();

    assertThat(dto.groupId()).isEqualTo(50L);
    assertThat(dto.groupName()).isEqualTo("Backend Team");
    assertThat(dto.groupDescription()).isEqualTo("Spring Boot developers");
    assertThat(dto.groupCode()).isEqualTo("ABC123456");
    assertThat(dto.memberCount()).isEqualTo(5);
  }
}