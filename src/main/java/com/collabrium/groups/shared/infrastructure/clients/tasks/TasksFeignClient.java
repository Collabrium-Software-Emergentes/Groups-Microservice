package com.collabrium.groups.shared.infrastructure.clients.tasks;

import com.collabrium.groups.shared.infrastructure.clients.tasks.resources.MemberOnlyResource;
import com.collabrium.groups.shared.infrastructure.clients.tasks.resources.MemberResource;
import com.collabrium.groups.shared.infrastructure.clients.tasks.resources.TaskResource;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@FeignClient(name = "tasks-service")
public interface TasksFeignClient {

  @GetMapping("/api/v1/member")
  List<MemberResource> getAllMembersByGroupId(
      @RequestParam Long groupId
  );

  @GetMapping("/api/v1/tasks")
  List<TaskResource> getAllTasksByGroupId(
      @RequestParam Long groupId
  );

  @GetMapping("/api/v1/member/{memberId}")
  MemberOnlyResource getMemberOnlyById(
      @PathVariable Long memberId
  );
}