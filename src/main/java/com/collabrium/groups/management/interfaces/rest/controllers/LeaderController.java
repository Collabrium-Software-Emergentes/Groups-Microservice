package com.collabrium.groups.management.interfaces.rest.controllers;

import com.collabrium.groups.management.domain.model.queries.GetLeaderByIdQuery;
import com.collabrium.groups.management.domain.services.LeaderQueryService;
import com.collabrium.groups.management.interfaces.rest.resources.LeaderResource;
import com.collabrium.groups.management.interfaces.rest.transform.LeaderResourceFromEntityAssembler;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/api/v1/leader")
@Tag(name = "Leaders", description = "Leader management API")
public class LeaderController {

  private final LeaderQueryService leaderQueryService;

  public LeaderController(
      LeaderQueryService leaderQueryService
  ) {

    this.leaderQueryService = leaderQueryService;
  }

  @GetMapping("/{leaderId}")
  @Operation(summary = "Get leader details by id", description = "Fetches the details of the leader.")
  public ResponseEntity<LeaderResource> getLeaderById(
      @PathVariable Long leaderId
  ) {

    var getLeaderByIdQuery = new GetLeaderByIdQuery(leaderId);
    var leaderOptional = leaderQueryService.handle(getLeaderByIdQuery);
    if (leaderOptional.isEmpty()) {
      return ResponseEntity.notFound().build();
    }
    var leaderResource = LeaderResourceFromEntityAssembler.toResourceFromEntity(leaderOptional.get());
    return ResponseEntity.ok(leaderResource);
  }
}
