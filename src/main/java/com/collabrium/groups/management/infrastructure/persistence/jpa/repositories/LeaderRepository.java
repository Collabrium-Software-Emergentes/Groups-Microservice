package com.collabrium.groups.management.infrastructure.persistence.jpa.repositories;

import com.collabrium.groups.management.domain.model.aggregates.Leader;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface LeaderRepository extends JpaRepository<Leader, Long> {

  Optional<Leader> findById(Long id);
}