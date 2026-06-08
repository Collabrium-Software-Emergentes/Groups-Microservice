package com.collabrium.groups.management.application.internal.outboundservices.ports;

import com.collabrium.groups.shared.infrastructure.clients.iam.resources.UserOnlyResource;

public interface IamQueryPort {

  UserOnlyResource getUserOnlyById(Long id);
  
  UserOnlyResource getUserByMemberId(Long memberId);
}