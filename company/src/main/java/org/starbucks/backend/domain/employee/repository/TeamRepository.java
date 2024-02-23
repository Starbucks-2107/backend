package org.starbucks.backend.domain.employee.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.starbucks.backend.domain.employee.entity.Team;

public interface TeamRepository extends JpaRepository<Team, Long> {
}
