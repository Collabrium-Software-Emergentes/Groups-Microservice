package com.collabrium.groups.management.domain.model.aggregates;

import com.collabrium.groups.management.domain.exceptions.InvalidInvitationException;
import com.collabrium.groups.management.domain.model.valueobjects.MemberId;
import com.collabrium.groups.shared.domain.model.aggregates.AuditableAbstractAggregateRoot;
import jakarta.persistence.AttributeOverride;
import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "invitations")
@Getter
@Setter
@NoArgsConstructor
public class Invitation extends AuditableAbstractAggregateRoot<Invitation> {

  @Embedded
  @AttributeOverride(name = "value", column = @Column(name = "member_id", nullable = false))
  private MemberId memberId;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "group_id", nullable = false)
  private Group group;

  public Invitation(MemberId memberId, Group group) {
    validateInvitationCreation(memberId, group);
    this.memberId = memberId;
    this.group = group;
  }

  private void validateInvitationCreation(MemberId memberId, Group group) {
    if (memberId == null) {
      throw InvalidInvitationException.forNullMemberId();
    }
    if (group == null) {
      throw InvalidInvitationException.forNullGroup();
    }
  }

  @Override
  public String toString() {
    return String.format("Invitation{id=%d, memberId=%s, groupId=%d}",
        getId() != null ? getId() : 0,
        memberId != null ? memberId : null,
        group != null && group.getId() != null ? group.getId() : 0
    );
  }
}