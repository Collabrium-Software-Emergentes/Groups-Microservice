package com.collabrium.groups.management.application.internal.queryservices;

import com.collabrium.groups.management.application.internal.outboundservices.ports.IamQueryPort;
import com.collabrium.groups.management.domain.exceptions.MemberNotFoundException;
import com.collabrium.groups.management.domain.exceptions.UserNotFoundException;
import com.collabrium.groups.management.domain.model.queries.GetInvitationByUserIdQuery;
import com.collabrium.groups.management.infrastructure.persistence.jpa.repositories.GroupRepository;
import com.collabrium.groups.management.infrastructure.persistence.jpa.repositories.InvitationRepository;
import com.collabrium.groups.shared.infrastructure.clients.iam.resources.UserOnlyResource;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
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
}