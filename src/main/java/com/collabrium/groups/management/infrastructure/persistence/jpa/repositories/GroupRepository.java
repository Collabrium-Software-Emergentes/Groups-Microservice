package com.collabrium.groups.management.infrastructure.persistence.jpa.repositories;

import com.collabrium.groups.management.domain.model.aggregates.Group;
import com.collabrium.groups.management.domain.model.valueobjects.GroupCode;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface GroupRepository extends JpaRepository<Group, Long> {

  Optional<Group> findByLeaderId(Long leader_id);

  boolean existsByCode(GroupCode code);

  Optional<Group> findByCode(GroupCode code);
}
