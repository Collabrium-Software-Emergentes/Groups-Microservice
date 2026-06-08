package com.collabrium.groups.management.infrastructure.persistence.jpa.repositories;

import com.collabrium.groups.management.domain.model.aggregates.Invitation;
import com.collabrium.groups.management.domain.model.valueobjects.MemberId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface InvitationRepository extends JpaRepository<Invitation, Long> {

  @Query("""
      SELECT i
      FROM Invitation i
      JOIN FETCH i.group
      WHERE i.memberId = :memberId
      """)
  Optional<Invitation> findByMemberId(MemberId memberId);

  @Query("""
      SELECT i
      FROM Invitation i
      JOIN FETCH i.group
      WHERE i.group.id = :groupId
      """)
  List<Invitation> findByGroupId(Long groupId);

  @Query("""
    SELECT i
    FROM Invitation i
    JOIN FETCH i.group g
    JOIN FETCH g.leader
    WHERE i.id = :invitationId
    """)
  Optional<Invitation> findDetailedById(Long invitationId);

  boolean existsByMemberId(MemberId memberId);
}