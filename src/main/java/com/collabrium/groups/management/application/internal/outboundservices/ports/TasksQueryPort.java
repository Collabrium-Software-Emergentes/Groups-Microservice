package com.collabrium.groups.management.application.internal.outboundservices.ports;

import com.collabrium.groups.shared.infrastructure.clients.tasks.resources.MemberOnlyResource;
import com.collabrium.groups.shared.infrastructure.clients.tasks.resources.MemberResource;
import com.collabrium.groups.shared.infrastructure.clients.tasks.resources.TaskResource;

import java.util.List;

public interface TasksQueryPort {

  List<MemberResource> getMembersByGroupId(Long groupId);

  List<TaskResource> getTasksByGroupId(Long groupId);

  MemberOnlyResource getMemberOnlyById(Long memberId);
}