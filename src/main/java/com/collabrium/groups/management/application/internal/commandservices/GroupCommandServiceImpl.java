package com.collabrium.groups.management.application.internal.commandservices;

import com.collabrium.groups.management.application.internal.outboundservices.messaging.GroupsEventPublisher;
import com.collabrium.groups.management.application.internal.outboundservices.ports.IamQueryPort;
import com.collabrium.groups.management.application.internal.outboundservices.ports.TasksQueryPort;
import com.collabrium.groups.management.domain.exceptions.*;
import com.collabrium.groups.management.domain.model.aggregates.Group;
import com.collabrium.groups.management.domain.model.commands.*;
import com.collabrium.groups.management.domain.model.events.GroupDeletedEvent;
import com.collabrium.groups.management.domain.model.events.RemoveMemberEvent;
import com.collabrium.groups.management.domain.model.valueobjects.GroupCode;
import com.collabrium.groups.management.domain.services.GroupCommandService;
import com.collabrium.groups.management.infrastructure.persistence.jpa.repositories.GroupRepository;
import com.collabrium.groups.management.infrastructure.persistence.jpa.repositories.LeaderRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * Service implementation for handling group-related commands.
 * Provides business logic for creating, updating, deleting groups,
 * and managing group membership operations.
 */
@Service
public class GroupCommandServiceImpl implements GroupCommandService {

  private final GroupRepository groupRepository;
  private final IamQueryPort iamQueryPort;
  private final LeaderRepository leaderRepository;
  private final TasksQueryPort tasksQueryPort;
  private final GroupsEventPublisher groupsEventPublisher;

  public GroupCommandServiceImpl(
      GroupRepository groupRepository,
      IamQueryPort iamQueryPort,
      LeaderRepository leaderRepository,
      TasksQueryPort tasksQueryPort,
      GroupsEventPublisher groupsEventPublisher
  ) {

    this.groupRepository = groupRepository;
    this.iamQueryPort = iamQueryPort;
    this.leaderRepository = leaderRepository;
    this.tasksQueryPort = tasksQueryPort;
    this.groupsEventPublisher = groupsEventPublisher;
  }

  /**
   * Creates a new group for a user.
   *
   * <p>This method validates that the user exists, has a leader profile,
   * does not already own a group, generates a unique group code,
   * and persists the new group entity.</p>
   *
   * @param command the create group command containing group name, description,
   *                image URL, and user ID
   * @return an Optional containing the created Group or empty if creation fails
   * @throws UserNotFoundException if the user does not exist
   * @throws InvalidLeaderException if the user is not a leader
   * @throws GroupAlreadyExistsException if the leader already owns a group
   */
  @Override
  public Optional<Group> handle(CreateGroupCommand command) {

    Long leaderId =
        getExistingLeaderId(command.userId());

    var leader =
        leaderRepository
            .findById(leaderId)
            .orElseThrow(() ->
                new LeaderNotFoundException(
                    leaderId
                )
            );

    validateLeaderDoesNotOwnGroup(
        leaderId
    );

    GroupCode groupCode =
        generateUniqueGroupCode();

    var group = new Group(
        command.name(),
        command.description(),
        command.imgUrl(),
        leader,
        groupCode
    );

    var savedGroup =
        groupRepository.save(group);

    return Optional.of(savedGroup);
  }

  /**
   * Updates an existing group's information.
   *
   * <p>This method validates that the user is a leader who owns a group,
   * then updates the group with the information provided in the command.</p>
   *
   * @param command the update group command containing new group information
   *                and user ID
   * @return an Optional containing the updated Group
   * @throws UserNotFoundException if the user does not exist
   * @throws InvalidLeaderException if the user is not a leader
   * @throws GroupNotFoundException if the leader does not own any group
   */
  @Override
  @Transactional
  public Optional<Group> handle(UpdateGroupCommand command) {

    Long leaderId =
        getExistingLeaderId(command.userId());

    var group =
        getLeaderGroup(leaderId);

    group.updateInformation(command);

    var updatedGroup =
        groupRepository.save(group);

    return Optional.of(updatedGroup);
  }

  /**
   * Deletes a group owned by a leader.
   *
   * <p>This method validates that the user is a leader who owns a group,
   * removes the group from the repository, and publishes a
   * {@link GroupDeletedEvent} to notify other services.</p>
   *
   * @param command the delete group command containing the user ID
   * @throws UserNotFoundException if the user does not exist
   * @throws InvalidLeaderException if the user is not a leader
   * @throws GroupNotFoundException if the leader does not own any group
   */
  @Override
  @Transactional
  public void handle(DeleteGroupCommand command) {

    Long leaderId =
        getExistingLeaderId(command.userId());

    var group =
        getLeaderGroup(leaderId);

    Long groupId =
        group.getId();

    groupRepository.delete(group);

    groupsEventPublisher.publishGroupDeleted(
        new GroupDeletedEvent(groupId)
    );
  }

  /**
   * Removes a member from the group owned by the authenticated leader.
   *
   * <p>This method validates that the leader exists and owns a group,
   * that the member exists and belongs to the group, then decreases
   * the group's member count, and publishes a {@link RemoveMemberEvent}.</p>
   *
   * @param command the remove member command containing leader's user ID
   *                and the member ID to remove
   * @throws UserNotFoundException if the user does not exist
   * @throws InvalidLeaderException if the user is not a leader
   * @throws GroupNotFoundException if the leader does not own any group
   * @throws MemberNotFoundException if the member does not exist
   * @throws InvalidGroupException if the member does not belong to the group
   */
  @Override
  @Transactional
  public void handle(RemoveMemberFromMyGroupCommand command) {

    Long leaderId =
        getExistingLeaderId(command.userId());

    var group =
        getLeaderGroup(leaderId);

    var member =
        tasksQueryPort.getMemberOnlyById(
            command.memberId()
        );

    if (member == null) {
      throw MemberNotFoundException
          .forMemberId(command.memberId());
    }

    validateMemberBelongsToGroup(
        member.groupId(),
        group.getId(),
        command.memberId()
    );

    group.decreaseMemberCount();

    groupsEventPublisher.publishMemberRemovedFromGroup(
        new RemoveMemberEvent(command.memberId())
    );
  }

  /**
   * Handles a member leaving a group.
   *
   * <p>This method finds the group by ID and decreases its member count.
   * No validation is performed on the member as this is handled at the controller level.</p>
   *
   * @param command the leave group command containing the group ID
   * @throws GroupNotFoundException if the group does not exist
   */
  @Override
  @Transactional
  public void handle(LeaveGroupCommand command) {

    var group =
        groupRepository
            .findById(command.groupId())
            .orElseThrow(() ->
                GroupNotFoundException.forId(
                    command.groupId()
                )
            );

    group.decreaseMemberCount();
  }

  /**
   * Retrieves an existing leader ID from a user ID.
   *
   * <p>This method validates that the user exists and has an associated
   * leader profile.</p>
   *
   * @param userId the user ID to look up
   * @return the associated leader ID
   * @throws UserNotFoundException if the user does not exist
   * @throws InvalidLeaderException if the user does not have a leader profile
   */
  private Long getExistingLeaderId(
      Long userId
  ) {

    var user =
        iamQueryPort.getUserOnlyById(userId);

    if (user == null) {
      throw UserNotFoundException.forId(
          userId
      );
    }

    if (user.leaderId() == null) {
      throw InvalidLeaderException
          .forUserIsNotLeader(userId);
    }

    return user.leaderId();
  }

  /**
   * Retrieves the group owned by a specific leader.
   *
   * @param leaderId the leader ID whose group to retrieve
   * @return the Group owned by the leader
   * @throws GroupNotFoundException if no group is found for the leader
   */
  private Group getLeaderGroup(
      Long leaderId
  ) {

    return groupRepository
        .findByLeaderId(leaderId)
        .orElseThrow(() ->
            GroupNotFoundException.forLeader(
                leaderId
            )
        );
  }

  /**
   * Validates that a leader does not already own a group.
   *
   * @param leaderId the leader ID to validate
   * @throws GroupAlreadyExistsException if the leader already owns a group
   */
  private void validateLeaderDoesNotOwnGroup(
      Long leaderId
  ) {

    if (
        groupRepository
            .findByLeaderId(leaderId)
            .isPresent()
    ) {

      throw new GroupAlreadyExistsException(
          "Leader already owns a group"
      );
    }
  }

  /**
   * Generates a unique group of code that does not already exist in the repository.
   *
   * <p>This method continuously generates random group codes until a unique one is found.</p>
   *
   * @return a unique GroupCode value object
   */
  private GroupCode generateUniqueGroupCode() {

    GroupCode groupCode;

    do {

      groupCode = GroupCode.generate();

    } while (
        groupRepository.existsByCode(groupCode)
    );

    return groupCode;
  }

  /**
   * Validates that a member belongs to a specific group.
   *
   * @param memberGroupId the group ID associated with the member
   * @param groupId the expected group ID
   * @param memberId the member ID (used for an exception message)
   * @throws InvalidGroupException if the member's group ID does not match the expected group ID
   */
  private void validateMemberBelongsToGroup(
      Long memberGroupId,
      Long groupId,
      Long memberId
  ) {

    if (
        memberGroupId == null ||
            !memberGroupId.equals(groupId)
    ) {

      throw InvalidGroupException
          .forMemberNotBelongingToGroup(
              memberId,
              groupId
          );
    }
  }
}
