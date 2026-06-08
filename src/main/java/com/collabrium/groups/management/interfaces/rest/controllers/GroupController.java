package com.collabrium.groups.management.interfaces.rest.controllers;

import com.collabrium.groups.management.domain.model.queries.GetGroupByCodeQuery;
import com.collabrium.groups.management.domain.model.queries.GetGroupByIdQuery;
import com.collabrium.groups.management.domain.model.queries.GetGroupByLeaderIdQuery;
import com.collabrium.groups.management.domain.services.GroupQueryService;
import com.collabrium.groups.management.interfaces.rest.resources.GroupResource;
import com.collabrium.groups.management.interfaces.rest.resources.GroupWithLeaderResource;
import com.collabrium.groups.management.interfaces.rest.transform.GroupResourceFromEntityAssembler;
import com.collabrium.groups.management.interfaces.rest.transform.GroupWithLeaderResourceFromEntityAssembler;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/api/v1/groups")
@Tag(name = "Groups", description = "Group management API")
public class GroupController {

  private final GroupQueryService groupQueryService;

  public GroupController(
      GroupQueryService groupQueryService
  ) {

    this.groupQueryService = groupQueryService;
  }

  @GetMapping("/{groupId}")
  @Operation(summary = "Search group by its id.", description = "Search group by id.")
  public ResponseEntity<GroupWithLeaderResource> getGroupById(
      @PathVariable Long groupId
  ) {

    var getGroupByIdQuery = new GetGroupByIdQuery(groupId);

    var groupOptional = groupQueryService.handle(getGroupByIdQuery);

    if (groupOptional.isEmpty()) {
      return ResponseEntity.notFound().build();
    }

    var groupResource = GroupWithLeaderResourceFromEntityAssembler.toResourceFromEntity(groupOptional.get());

    return ResponseEntity.ok(groupResource);
  }

  @GetMapping("/search")
  @Operation(summary = "Search for a group by code", description = "Search for a group by code")
  public ResponseEntity<GroupResource> searchGroupByCode(
      @RequestParam String code
  ) {

    var getGroupByCodeQuery = new GetGroupByCodeQuery(code);

    var groupOptional = groupQueryService.handle(getGroupByCodeQuery);

    if (groupOptional.isEmpty()) {
      return ResponseEntity.notFound().build();
    }

    var groupResource = GroupResourceFromEntityAssembler.toResourceFromEntity(groupOptional.get());

    return ResponseEntity.ok(groupResource);
  }

  @GetMapping
  public ResponseEntity<GroupWithLeaderResource> getGroupByLeaderId(
      @RequestParam Long leaderId
  ) {

    var getGroupByLeaderIdQuery = new GetGroupByLeaderIdQuery(leaderId);

    var groupOptional = groupQueryService.handle(getGroupByLeaderIdQuery);

    if (groupOptional.isEmpty()) {
      return ResponseEntity.notFound().build();
    }

    var groupResource = GroupWithLeaderResourceFromEntityAssembler.toResourceFromEntity(groupOptional.get());

    return ResponseEntity.ok(groupResource);
  }
}
