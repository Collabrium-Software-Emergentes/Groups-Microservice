package com.collabrium.groups.management.interfaces.rest.controllers;

import com.collabrium.groups.management.application.internal.queryservices.LeaderDetailsQueryService;
import com.collabrium.groups.management.domain.model.queries.GetLeaderDetailsByUserIdQuery;
import com.collabrium.groups.management.domain.model.queries.GetMembersOfMyGroupQuery;
import com.collabrium.groups.management.domain.model.queries.GetTasksOfMyGroupQuery;
import com.collabrium.groups.management.interfaces.rest.resources.LeaderDetailsResource;
import com.collabrium.groups.management.interfaces.rest.resources.MembersDetailsResource;
import com.collabrium.groups.management.interfaces.rest.resources.TaskDetailsResource;
import com.collabrium.groups.management.interfaces.rest.transform.LeaderDetailsResourceFromDTOAssembler;
import com.collabrium.groups.management.interfaces.rest.transform.MemberDetailsResourceFromDTOAssembler;
import com.collabrium.groups.management.interfaces.rest.transform.TaskDetailsResourceFromDTOAssembler;
import com.collabrium.groups.shared.infrastructure.security.AuthenticatedUser;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(value = "/api/v1")
@Tag(name = "Leaders Details", description = "Leader details management API")
public class LeaderDetailsController {

  private final LeaderDetailsQueryService leaderDetailsQueryService;

  public LeaderDetailsController(
      LeaderDetailsQueryService leaderDetailsQueryService
  ) {

    this.leaderDetailsQueryService = leaderDetailsQueryService;
  }

  @GetMapping("/leader/details")
  @Operation(
      summary = "Get authenticated leader details",
      description = "Returns the details of the authenticated leader"
  )
  public ResponseEntity<LeaderDetailsResource> getLeaderDetailsByUserId(
      @AuthenticationPrincipal AuthenticatedUser user
  ) {

    var query = new GetLeaderDetailsByUserIdQuery(user.userId());

    var leaderDetailsOptional = leaderDetailsQueryService.handle(query);

    if (leaderDetailsOptional.isEmpty()) {
      return ResponseEntity.notFound().build();
    }

    var leaderDetailsResource = LeaderDetailsResourceFromDTOAssembler
        .toResourceFromDTO(leaderDetailsOptional.get());

    return ResponseEntity.ok(leaderDetailsResource);
  }

  @GetMapping("/groups/members")
  @Operation(
      summary = "Get all group members",
      description = "Retrieve all members of a group"
  )
  public ResponseEntity<List<MembersDetailsResource>> getAllMembersByGroupId(
      @AuthenticationPrincipal AuthenticatedUser user
  ) {

    var getMembersOfMyGroupQuery = new GetMembersOfMyGroupQuery(user.userId());

    var membersDetails = leaderDetailsQueryService.handle(getMembersOfMyGroupQuery);

    var membersResources = membersDetails
        .stream()
        .map(MemberDetailsResourceFromDTOAssembler::toResourceFromDTO)
        .toList();

    return ResponseEntity.ok(membersResources);
  }

  @GetMapping("/groups/tasks")
  @Operation(
      summary = "Get all group tasks",
      description = "Retrieve all tasks of the authenticated leader's group"
  )
  public ResponseEntity<List<TaskDetailsResource>> getAllTasksByGroupId(
      @AuthenticationPrincipal AuthenticatedUser user
  ) {

    var query =
        new GetTasksOfMyGroupQuery(
            user.userId()
        );

    var tasksDetails =
        leaderDetailsQueryService.handle(query);

    var tasksResources =
        tasksDetails.stream()
            .map(TaskDetailsResourceFromDTOAssembler::toResourceFromDTO)
            .toList();

    return ResponseEntity.ok(tasksResources);
  }
}
