package org.starbucks.backend.domain.employee.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.starbucks.backend.domain.employee.entity.Department;

import java.util.Optional;

public interface DepartmentRepository extends JpaRepository<Department, Long> {
    Optional<Department> findByDepartment(String department);
}
